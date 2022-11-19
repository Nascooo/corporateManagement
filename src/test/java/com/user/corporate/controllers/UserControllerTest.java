package com.user.corporate.controllers;

import com.user.corporate.entities.User;
import com.user.corporate.exception.GlobalExceptionHandler;
import com.user.corporate.mappers.UserMapper;
import com.user.corporate.models.UserModel;
import com.user.corporate.requests.CorporateRequest;
import com.user.corporate.responses.UserCreationResponse;
import com.user.corporate.rest.controllers.UserController;
import com.user.corporate.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;


    private MultipartFile[] createMultipartFilesMockWithTypes(String type1, String type2) {
        MockMultipartFile multipartFile1 = new MockMultipartFile("pdf", "file.pdf", type1,
                "Hello".getBytes());
        MockMultipartFile multipartFile2 = new MockMultipartFile("Image", "img.jpeg", type2,
                "Hello".getBytes());
        MultipartFile[] response = {multipartFile1, multipartFile2};
        return response;
    }

    @Test
    void test_corporate_request_Success() {
        //Given
        CorporateRequest corporateRequest = new CorporateRequest();
        UserModel john = populateUserModel("John", 123, new Date(1762812000000L));
        corporateRequest.setUser(john);
        corporateRequest.setMultipartFiles(createMultipartFilesMockWithTypes(APPLICATION_PDF_VALUE, IMAGE_JPEG_VALUE));
        corporateRequest.setRequestName("Request_name");
        User user = populateUser(john.getName(), john.getCivilId(), john.getExpiryDate());
        UserCreationResponse response = new UserCreationResponse();
        response.setName(user.getName());
        response.setCivilId(user.getCivilId());
        response.setExpiryDate(user.getExpiryDate());

        //WHEN
        when(userMapper.fromUserModelToUser(corporateRequest.getUser())).thenReturn(user);
        when(userService.createUser(any())).thenReturn(user);
        when(userMapper.fromUserToUserResponse(any())).thenReturn(response);
        UserCreationResponse userCreationResponse = userController.corporateRequest(corporateRequest);

        //THEN
        assertNotNull(userCreationResponse);
        assertEquals(user.getName(), userCreationResponse.getName());
        assertEquals(user.getCivilId(), userCreationResponse.getCivilId());
        assertEquals(user.getExpiryDate(), userCreationResponse.getExpiryDate());
    }

    @Test
    void test_corporate_request_fail_when_attachments_contain_only_one_type() {
        CorporateRequest corporateRequest = new CorporateRequest();
        UserModel john = populateUserModel("John", 123, new Date(1762812000000L));
        corporateRequest.setUser(john);
        corporateRequest.setMultipartFiles(createMultipartFilesMockWithTypes(IMAGE_JPEG_VALUE, IMAGE_JPEG_VALUE));
        corporateRequest.setRequestName("Request_name");
        User user = populateUser(john.getName(), john.getCivilId(), john.getExpiryDate());

        //WHEN
        when(userMapper.fromUserModelToUser(corporateRequest.getUser())).thenReturn(user);

        assertThrows(GlobalExceptionHandler.class, () -> userController.corporateRequest(corporateRequest));


    }

    @Test
    void test_corporate_request_fail_when_civilID_expired() {
        CorporateRequest corporateRequest = new CorporateRequest();
        UserModel john = populateUserModel("John", 123, new Date(1662812000000L));
        corporateRequest.setUser(john);
        corporateRequest.setMultipartFiles(createMultipartFilesMockWithTypes(IMAGE_JPEG_VALUE, IMAGE_JPEG_VALUE));
        corporateRequest.setRequestName("Request_name");
        User user = populateUser(john.getName(), john.getCivilId(), john.getExpiryDate());

        //WHEN
        when(userMapper.fromUserModelToUser(corporateRequest.getUser())).thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userController.corporateRequest(corporateRequest));
        assertTrue(exception.getMessage().contains("Civil"));

    }


    @Test
    void test_add_user() {
        //Given
        User user = populateUser("John", 123, new Date(1762812000000L));
        UserModel userModel = populateUserModel(user.getName(), user.getCivilId(), user.getExpiryDate());
        //when
        when(userService.createUser(any())).thenReturn(user);
        User createdUser = userController.createUser(userModel);

        //then
        verify(userMapper).fromUserModelToUser(any());
        assertNotNull(createdUser);
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getExpiryDate(), createdUser.getExpiryDate());
        assertEquals(user.getCivilId(), createdUser.getCivilId());
        assertNull(createdUser.getUserDocuments());
    }

    @Test
    void test_update_user() {
        //Given
        User user = populateUser("John", 123, new Date(1762812000000L));
        UserModel userModel = populateUserModel("Ahmed", null, null);
        User expectedUser = populateUser(userModel.getName(), user.getCivilId(), user.getExpiryDate());

        //When
        when(userMapper.fromUserModelToUser(userModel)).thenReturn(user);
        when(userService.updateUser(1L, user)).thenReturn(expectedUser);

        User userUpdated = userController.updateUser(1L, userModel);

        //then
        assertNotNull(userUpdated);
        assertEquals(userModel.getName(), userUpdated.getName());
        assertEquals(user.getExpiryDate(), userUpdated.getExpiryDate());
    }

    @Test
    void test_get_user() {
        //Given
        User savedUser = populateUser("John", 123, new Date(1762812000000L));
        savedUser.setUserId(1L);

        //WHEN
        when(userService.getUserById(anyLong())).thenReturn(savedUser);

        User user = userController.getUser(1L);

        //THEN
        assertNotNull(user);
        assertEquals(savedUser.getName(), user.getName());
        assertEquals(savedUser.getCivilId(), user.getCivilId());
    }

    @Test
    void test_delete_user() {
        userController.deleteUser(1L);
        verify(userService, times(1)).deleteUserById(anyLong());
    }

    private UserModel populateUserModel(String name, Integer civilId, Date expiryDate) {
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setCivilId(civilId);
        userModel.setExpiryDate(expiryDate);
        return userModel;
    }

    private User populateUser(String name, Integer civilId, Date expiryDate) {
        User user = new User();
        user.setName(name);
        user.setCivilId(civilId);
        user.setExpiryDate(expiryDate);
        return user;
    }

}
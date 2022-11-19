package com.user.corporate.rest.controllers;

import com.user.corporate.entities.User;
import com.user.corporate.entities.UserDocuments;
import com.user.corporate.exception.GlobalExceptionHandler;
import com.user.corporate.mappers.UserMapper;
import com.user.corporate.models.UserModel;
import com.user.corporate.requests.CorporateRequest;
import com.user.corporate.responses.UserCreationResponse;
import com.user.corporate.rest.api.UserApi;
import com.user.corporate.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class UserController implements UserApi {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService;
    private UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public UserCreationResponse corporateRequest(CorporateRequest corporateRequest) {
        validateRequest(corporateRequest);
        User user = userMapper.fromUserModelToUser(corporateRequest.getUser());
        List<UserDocuments> userDocuments = new ArrayList<>();
        for (MultipartFile file : corporateRequest.getMultipartFiles()) {
            UserDocuments document = new UserDocuments();
            try {
                document.setDocument(file.getBytes());
            } catch (IOException exception) {
                logger.error(exception.getMessage());
            }
            userDocuments.add(document);
        }
        user.setUserDocuments(userDocuments);
        User createdUser = userService.createUser(user);
        return userMapper.fromUserToUserResponse(createdUser);
    }

    @Override
    public User createUser(UserModel userModel) {
        User user = userMapper.fromUserModelToUser(userModel);
        return userService.createUser(user);
    }

    @Override
    public User updateUser(Long userId, UserModel userModel) {
        User user = userMapper.fromUserModelToUser(userModel);
        return userService.updateUser(userId, user);
    }

    @Override
    public void deleteUser(Long userId) {
        userService.deleteUserById(userId);
    }

    @Override
    public User getUser(Long userId) {
        return userService.getUserById(userId);
    }

    private void validateRequest(CorporateRequest corporateRequest) {
        Assert.notNull(corporateRequest.getUser(), "user Should Not Be null");
        Date expiryDate = corporateRequest.getUser().getExpiryDate();
        Assert.notNull(expiryDate, "Expiry Date Should Exist");
        Assert.notNull(corporateRequest.getMultipartFiles(), "Expiry Date Should Exist");
        Assert.isTrue(corporateRequest.getMultipartFiles().length > 1, "User Should Add at least 2 Documents");
        Assert.isTrue(corporateRequest.getMultipartFiles().length < 5, "User Should Add at Most 4 Documents");
        //Validate Expiry Date of Civil ID
        Assert.isTrue(expiryDate.after(new Date()), "Expired Civil Id");

        //Validate Attachment types
        long attachmentTypesNumber = Arrays.stream(corporateRequest.getMultipartFiles()).map(MultipartFile::getContentType).distinct().count();
        if (attachmentTypesNumber < 2) {
            throw new GlobalExceptionHandler("at least required two different Types");
        }

    }
}

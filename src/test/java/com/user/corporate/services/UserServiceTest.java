package com.user.corporate.services;

import com.user.corporate.entities.User;
import com.user.corporate.mappers.UserMapper;
import com.user.corporate.repositories.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserMapper userMapper;


    private User populateUser(String name, Integer civilId, Date expiryDate) {
        User user = new User();
        user.setName(name);
        user.setCivilId(civilId);
        user.setExpiryDate(expiryDate);
        return user;
    }

    @Test
    void test_create_user() {
        User user = populateUser("Ahmed", 1234, new Date(1762812000000L));

        when(userRepo.save(any())).thenReturn(user);

        User savedUser = userService.createUser(user);

        verify(userRepo).save(user);
        assertEquals(user.getName(), savedUser.getName());
    }

    @Test
    void test_upate_user_name() {
        User existUser = populateUser("Ahmed", 1234, new Date(1762812000000L));
        User userToUpdate = populateUser("Mohamed", null, null);

        when(userRepo.findById(1L)).thenReturn(Optional.of(existUser));
        doNothing().when(userMapper).mergeUserAndIgnoreNull(any(), any());
        userService.updateUser(1L, userToUpdate);

        verify(userMapper).mergeUserAndIgnoreNull(userToUpdate, existUser);
    }

    @Test
    void test_delete_by_id() {
        User existUser = populateUser("Ahmed", 1234, new Date(1762812000000L));

        when(userRepo.findById(1L)).thenReturn(Optional.of(existUser));

        userService.deleteUserById(1L);

        verify(userRepo).deleteById(1L);
    }

    @Test
    void get_user_by_id() {

        User existUser = populateUser("Ahmed", 1234, new Date(1762812000000L));

        when(userRepo.findById(1L)).thenReturn(Optional.of(existUser));

        User savedUser = userService.getUserById(1L);

        assertNotNull(savedUser);

    }
}
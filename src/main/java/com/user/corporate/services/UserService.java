package com.user.corporate.services;

import com.user.corporate.entities.User;
import com.user.corporate.exception.GlobalExceptionHandler;
import com.user.corporate.mappers.UserMapper;
import com.user.corporate.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserMapper userMapper;

    public User createUser(User user) {
        return userRepo.save(user);
    }


    /**
     * @param userId
     * @param user   expiryDate cannot be updated to null
     * @return
     */
    public User updateUser(Long userId, User user) {
        User savedUser = getUserById(userId);
        userMapper.mergeUserAndIgnoreNull(user, savedUser);
        return savedUser;
    }

    public void deleteUserById(Long userId) {
        getUserById(userId);
        userRepo.deleteById(userId);
    }

    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new GlobalExceptionHandler("User Not Exist"));
    }
}

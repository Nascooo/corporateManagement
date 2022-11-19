package com.user.corporate.rest.api;


import com.user.corporate.entities.User;
import com.user.corporate.models.UserModel;
import com.user.corporate.requests.CorporateRequest;
import com.user.corporate.responses.UserCreationResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/user")
public interface UserApi {


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    UserCreationResponse corporateRequest(@ModelAttribute CorporateRequest corporateRequest);

    @PostMapping
    User createUser(@RequestBody UserModel userModel);

    @PatchMapping(value = "{id}")
    User updateUser(@PathVariable("id") Long userId, @RequestBody UserModel userModel);

    @DeleteMapping(value = "{id}")
    void deleteUser(@PathVariable("id") Long userId);

    @GetMapping(value = "{id}")
    User getUser(@PathVariable("id") Long userId);

}

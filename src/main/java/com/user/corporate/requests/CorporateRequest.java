package com.user.corporate.requests;

import com.user.corporate.entities.User;
import com.user.corporate.models.UserModel;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class CorporateRequest {
    private String requestName;
    private Integer requestStatus;
    private UserModel user;
    private MultipartFile[] multipartFiles;
}

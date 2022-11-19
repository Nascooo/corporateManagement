package com.user.corporate.responses;

import lombok.Data;

import java.util.Date;


@Data
public class UserCreationResponse {

    private Long userId;

    private String name;

    private Integer civilId;

    private Date expiryDate;
}

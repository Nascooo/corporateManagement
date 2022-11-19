package com.user.corporate.exception;

import lombok.Data;

@Data
public class GeneralErrorDTO {

    private String message;
    private Integer errorCode;

}

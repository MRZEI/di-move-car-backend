package com.dimovecar.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;

    private Long userId;

    private UserVO userInfo;
}

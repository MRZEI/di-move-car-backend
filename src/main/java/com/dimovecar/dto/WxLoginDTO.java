package com.dimovecar.dto;

import lombok.Data;

@Data
public class WxLoginDTO {

    private String code;

    private String encryptedData;

    private String iv;

    private String userInfo;
}

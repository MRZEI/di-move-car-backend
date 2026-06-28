package com.dimovecar.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private String realName;
    private Integer isVerified;
    private Integer status;
    private Integer notifyCount;
    private BigDecimal successRate;
    private BigDecimal rating;
}

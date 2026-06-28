package com.dimovecar.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSettingVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private Integer notifyEnable;
    private Integer soundEnable;
    private Integer vibrateEnable;
    private String quietStart;
    private String quietEnd;
    private Integer quietEnable;
}

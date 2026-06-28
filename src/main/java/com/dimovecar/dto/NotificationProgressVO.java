package com.dimovecar.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class NotificationProgressVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer step;
    private String title;
    private String description;
    private Integer status;
    private String statusDesc;
    private LocalDateTime createTime;
    private LocalDateTime completeTime;
}

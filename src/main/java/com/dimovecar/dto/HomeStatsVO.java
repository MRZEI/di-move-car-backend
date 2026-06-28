package com.dimovecar.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class HomeStatsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer monthlyNotifications;
    private Integer avgResponseMinutes;
    private String successRate;
}

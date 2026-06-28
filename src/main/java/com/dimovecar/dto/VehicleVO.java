package com.dimovecar.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class VehicleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String plateNumber;
    private Integer vehicleType;
    private String vehicleTypeDesc;
    private String brand;
    private String model;
    private String color;
    private Integer isDefault;
    private String qrCodeUrl;
    private LocalDateTime createTime;
}

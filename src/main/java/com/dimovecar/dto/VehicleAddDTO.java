package com.dimovecar.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class VehicleAddDTO {

    @NotBlank(message = "车牌号不能为空")
    private String plateNumber;

    @NotNull(message = "车辆类型不能为空")
    private Integer vehicleType;

    private String brand;

    private String model;

    private String color;

    private Integer isDefault;
}

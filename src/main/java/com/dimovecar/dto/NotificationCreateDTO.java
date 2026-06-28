package com.dimovecar.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class NotificationCreateDTO {

    @NotBlank(message = "车牌号不能为空")
    private String plateNumber;

    private String location;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String parkingInfo;

    private String remark;

    private String imageUrls;
}

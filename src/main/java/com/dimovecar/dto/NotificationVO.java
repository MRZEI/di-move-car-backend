package com.dimovecar.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NotificationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long initiatorId;
    private Long targetUserId;
    private Long targetVehicleId;
    private String plateNumber;
    private Integer notifyType;
    private String notifyTypeDesc;
    private Integer status;
    private String statusDesc;
    private String location;
    private String parkingInfo;
    private String remark;
    private Integer responseTime;
    private LocalDateTime completeTime;
    private LocalDateTime createTime;
    private String timeAgo;
    private List<NotificationProgressVO> progressList;
}

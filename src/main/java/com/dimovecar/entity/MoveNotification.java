package com.dimovecar.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("move_notification")
public class MoveNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long initiatorId;

    private Long targetUserId;

    private Long targetVehicleId;

    private String plateNumber;

    private Integer notifyType;

    private Integer status;

    private String location;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String parkingInfo;

    private String remark;

    private String imageUrls;

    private Integer notifyChannel;

    private Integer responseTime;

    private LocalDateTime completeTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}

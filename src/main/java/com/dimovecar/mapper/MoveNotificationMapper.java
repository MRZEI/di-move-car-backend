package com.dimovecar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dimovecar.entity.MoveNotification;
import com.dimovecar.dto.NotificationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MoveNotificationMapper extends BaseMapper<MoveNotification> {

    IPage<NotificationVO> selectNotificationPage(Page<NotificationVO> page,
                                                   @Param("userId") Long userId,
                                                   @Param("status") Integer status,
                                                   @Param("notifyType") Integer notifyType);

    NotificationVO selectNotificationDetail(@Param("id") Long id, @Param("userId") Long userId);
}

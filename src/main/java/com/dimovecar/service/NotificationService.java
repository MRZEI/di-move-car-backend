package com.dimovecar.service;

import com.dimovecar.common.PageResult;
import com.dimovecar.dto.HomeStatsVO;
import com.dimovecar.dto.NotificationCreateDTO;
import com.dimovecar.dto.NotificationVO;

public interface NotificationService {

    PageResult<NotificationVO> getNotificationList(Integer pageNum, Integer pageSize, Integer status, Integer notifyType);

    NotificationVO getNotificationDetail(Long id);

    NotificationVO createNotification(NotificationCreateDTO dto);

    void remindAgain(Long id);

    void completeNotification(Long id);

    void cancelNotification(Long id);

    HomeStatsVO getHomeStats();
}

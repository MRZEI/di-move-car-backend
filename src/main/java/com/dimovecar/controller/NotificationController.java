package com.dimovecar.controller;

import com.dimovecar.common.PageResult;
import com.dimovecar.common.Result;
import com.dimovecar.dto.HomeStatsVO;
import com.dimovecar.dto.NotificationCreateDTO;
import com.dimovecar.dto.NotificationVO;
import com.dimovecar.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "通知接口")
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @ApiOperation("获取通知列表")
    @GetMapping("/list")
    public Result<PageResult<NotificationVO>> getNotificationList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer notifyType) {
        PageResult<NotificationVO> page = notificationService.getNotificationList(pageNum, pageSize, status, notifyType);
        return Result.success(page);
    }

    @ApiOperation("获取通知详情")
    @GetMapping("/{id}")
    public Result<NotificationVO> getNotificationDetail(@PathVariable Long id) {
        NotificationVO vo = notificationService.getNotificationDetail(id);
        return Result.success(vo);
    }

    @ApiOperation("创建挪车通知")
    @PostMapping("/create")
    public Result<NotificationVO> createNotification(@Validated @RequestBody NotificationCreateDTO dto) {
        NotificationVO vo = notificationService.createNotification(dto);
        return Result.success(vo);
    }

    @ApiOperation("再次提醒")
    @PostMapping("/{id}/remind")
    public Result<Void> remindAgain(@PathVariable Long id) {
        notificationService.remindAgain(id);
        return Result.success();
    }

    @ApiOperation("完成挪车")
    @PostMapping("/{id}/complete")
    public Result<Void> completeNotification(@PathVariable Long id) {
        notificationService.completeNotification(id);
        return Result.success();
    }

    @ApiOperation("取消通知")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancelNotification(@PathVariable Long id) {
        notificationService.cancelNotification(id);
        return Result.success();
    }

    @ApiOperation("首页统计数据")
    @GetMapping("/home-stats")
    public Result<HomeStatsVO> getHomeStats() {
        HomeStatsVO vo = notificationService.getHomeStats();
        return Result.success(vo);
    }
}

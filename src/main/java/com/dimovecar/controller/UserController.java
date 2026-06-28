package com.dimovecar.controller;

import com.dimovecar.common.Result;
import com.dimovecar.dto.UserSettingVO;
import com.dimovecar.dto.UserVO;
import com.dimovecar.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        UserVO vo = userService.getUserInfo();
        return Result.success(vo);
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody UserVO userVO) {
        userService.updateUserInfo(userVO);
        return Result.success();
    }

    @ApiOperation("获取用户设置")
    @GetMapping("/setting")
    public Result<UserSettingVO> getUserSetting() {
        UserSettingVO vo = userService.getUserSetting();
        return Result.success(vo);
    }

    @ApiOperation("更新用户设置")
    @PutMapping("/setting")
    public Result<Void> updateUserSetting(@RequestBody UserSettingVO settingVO) {
        userService.updateUserSetting(settingVO);
        return Result.success();
    }

    @ApiOperation("获取个人中心数据")
    @GetMapping("/profile")
    public Result<Map<String, Object>> getProfileData() {
        Map<String, Object> data = userService.getProfileData();
        return Result.success(data);
    }
}

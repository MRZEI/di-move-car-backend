package com.dimovecar.controller;

import com.dimovecar.common.Result;
import com.dimovecar.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "短信接口")
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private UserService userService;

    @ApiOperation("发送短信验证码")
    @PostMapping("/send-code")
    public Result<Void> sendSmsCode(@RequestBody Map<String, Object> params) {
        String phone = (String) params.get("phone");
        Integer type = params.get("type") != null ? Integer.valueOf(params.get("type").toString()) : 1;
        userService.sendSmsCode(phone, type);
        return Result.success();
    }
}

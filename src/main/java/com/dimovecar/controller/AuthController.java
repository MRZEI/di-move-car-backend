package com.dimovecar.controller;

import com.dimovecar.common.Result;
import com.dimovecar.dto.LoginDTO;
import com.dimovecar.dto.LoginVO;
import com.dimovecar.dto.UserVO;
import com.dimovecar.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "认证接口")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @ApiOperation("手机号验证码登录")
    @PostMapping("/login")
    public Result<LoginVO> loginByPhone(@Validated @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = authService.loginByPhone(loginDTO);
        return Result.success(loginVO);
    }

    @ApiOperation("微信登录")
    @PostMapping("/wx-login")
    public Result<LoginVO> wxLogin(@RequestBody Map<String, String> params) {
        String code = params.get("code");
        String userInfo = params.get("userInfo");
        LoginVO loginVO = authService.wxLogin(code, userInfo);
        return Result.success(loginVO);
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.success();
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/me")
    public Result<UserVO> getCurrentUser() {
        UserVO userVO = authService.getCurrentUser();
        return Result.success(userVO);
    }
}

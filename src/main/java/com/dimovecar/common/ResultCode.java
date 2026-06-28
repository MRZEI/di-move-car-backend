package com.dimovecar.common;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或token已过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "请求的资源不存在"),

    USER_NOT_EXIST(1001, "用户不存在"),
    USER_ALREADY_EXIST(1002, "用户已存在"),
    PHONE_ALREADY_EXIST(1003, "手机号已注册"),
    PHONE_OR_PASSWORD_ERROR(1004, "手机号或密码错误"),
    SMS_CODE_ERROR(1005, "验证码错误或已过期"),
    SMS_SEND_TOO_FREQUENT(1006, "验证码发送过于频繁"),

    VEHICLE_NOT_EXIST(2001, "车辆不存在"),
    VEHICLE_ALREADY_EXIST(2002, "该车牌已被绑定"),
    PLATE_FORMAT_ERROR(2003, "车牌号格式错误"),

    NOTIFICATION_NOT_EXIST(3001, "通知不存在"),
    NOTIFICATION_STATUS_ERROR(3002, "通知状态不允许此操作"),

    PARAM_ERROR(4001, "参数错误"),
    ;

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

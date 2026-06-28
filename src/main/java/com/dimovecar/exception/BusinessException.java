package com.dimovecar.exception;

import com.dimovecar.common.ResultCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.FAIL.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }
}

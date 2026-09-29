package com.chalnakchalnak.member_service.auth.common.exception;

import com.chalnakchalnak.member_service.auth.common.response.BaseResponseStatus;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{

    private final BaseResponseStatus status;
    public BaseException(BaseResponseStatus status) {
        this.status = status;
    }
}
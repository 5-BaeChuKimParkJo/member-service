package com.chalnakchalnak.member_service.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseResponseEntity<T>(@Nullable T result) {

    public BaseResponseEntity() {
        this(null);
    }

}
package com.chalnakchalnak.member_service.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Honor {     // 수정해라!!!!

    NICE_GUY("멋진남자"),
    GOOD_BOY("착한소년"),
    REAL_MAN("진짜사나이");

    private final String label;
}

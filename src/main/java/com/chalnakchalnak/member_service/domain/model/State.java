package com.chalnakchalnak.member_service.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum State {
    ACTIVE("활성화"),
    INACTIVE("비활성화"),
    BLOCKED("차단");

    private final String label;
}

package com.chalnakchalnak.member_service.auth.application.port.out;

import com.chalnakchalnak.member_service.auth.application.port.dto.StoreRefreshTokenDto;

public interface TokenStorePort {

    void saveRefreshToken(StoreRefreshTokenDto storeRefreshTokenDto);
    String getRefreshToken(String memberUuid);
    void deleteRefreshToken(String memberUuid);
}

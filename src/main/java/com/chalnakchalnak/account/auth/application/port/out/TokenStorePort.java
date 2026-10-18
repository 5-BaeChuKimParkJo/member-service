package com.chalnakchalnak.account.auth.application.port.out;

import com.chalnakchalnak.account.auth.application.port.dto.StoreRefreshTokenDto;

public interface TokenStorePort {

    void saveRefreshToken(StoreRefreshTokenDto storeRefreshTokenDto);
    String getRefreshToken(String memberUuid);
    void deleteRefreshToken(String memberUuid);
}

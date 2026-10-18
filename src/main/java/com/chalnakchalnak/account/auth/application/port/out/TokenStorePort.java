package com.chalnakchalnak.account.auth.application.port.out;

import com.chalnakchalnak.account.auth.application.command.StoreRefreshTokenCommand;

public interface TokenStorePort {

  void saveRefreshToken(StoreRefreshTokenCommand storeRefreshTokenDto);

  String getRefreshToken(String memberUuid);

  void deleteRefreshToken(String memberUuid);
}

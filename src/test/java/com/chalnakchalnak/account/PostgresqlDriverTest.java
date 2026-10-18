package com.chalnakchalnak.account;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class PostgresqlDriverTest {

  @Test
  void postgresqlDriverIsAvailableAtRuntime() {
    assertDoesNotThrow(() -> Class.forName("org.postgresql.Driver"));
  }
}

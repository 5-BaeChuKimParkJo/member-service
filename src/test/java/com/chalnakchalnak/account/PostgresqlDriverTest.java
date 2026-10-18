package com.chalnakchalnak.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PostgresqlDriverTest {

    @Test
    void postgresqlDriverIsAvailableAtRuntime() {
        assertDoesNotThrow(() -> Class.forName("org.postgresql.Driver"));
    }
}

package io.ylab.walletservice.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordHasherTest {

    @Test
    @DisplayName("should hash password and return byte[] password")
    void shouldReturnHashedPassword() {
        String password = "test password!!!";
        byte[] hashedPassword = PasswordHasher.hashPassword(password);

        assertThat(hashedPassword).isNotNull().hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("should compare different passwords and return false")
    void shouldReturnFalseWhenCompareDifferentPasswords() {
        String password = "fakePassword";
        String originalPassword = "original";

        byte[] hashedPassword = PasswordHasher.hashPassword(originalPassword);

        assertThat(PasswordHasher.checkPassword(password, hashedPassword)).isFalse();
    }
    @Test
    @DisplayName("should compare the same passwords and return true")
    void shouldReturnTrueWhenCompareSamePasswords() {
        String password = "original";
        String originalPassword = "original";

        byte[] hashedPassword = PasswordHasher.hashPassword(originalPassword);

        assertThat(PasswordHasher.checkPassword(password, hashedPassword)).isTrue();
    }
}
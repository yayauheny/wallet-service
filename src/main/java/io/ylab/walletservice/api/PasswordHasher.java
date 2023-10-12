package io.ylab.walletservice.api;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

/**
 * The {@link PasswordHasher} utility class provides methods for hashing and checking passwords
 * using the BCrypt password hashing algorithm.
 */
@UtilityClass
public class PasswordHasher {

    /**
     * Hashes the plain password using the BCrypt algorithm.
     *
     * @param plainPassword The plain password to be hashed.
     * @return The hashed password as a byte array.
     */
    public static byte[] hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hash(6, plainPassword.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Checks if the provided plain password matches the hashed password.
     *
     * @param plainPassword  The plain password to be checked.
     * @param hashedPassword The hashed password to be compared against.
     * @return {@code true} if the passwords match, {@code false} otherwise.
     */
    public static boolean checkPassword(String plainPassword, byte[] hashedPassword) {
        return BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified;
    }
}


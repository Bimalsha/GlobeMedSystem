package com.globemed.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility for hashing and verifying passwords using a salted SHA-256 approach.
 * IMPORTANT: For production, a stronger, slower algorithm like BCrypt or Argon2 is recommended.
 */
public class PasswordUtil {

    private static final int SALT_LENGTH = 16;

    /**
     * Hashes a plain-text password with a random salt.
     * @param plainPassword The plain-text password.
     * @return The salted and hashed password string in the format "salt:hash".
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            return null;
        }
        byte[] salt = generateSalt();
        byte[] hashedPassword = hashWithSalt(plainPassword.getBytes(), salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hashedPassword);
    }

    /**
     * Verifies a plain-text password against a stored hashed password.
     * @param plainPassword The plain-text password to verify.
     * @param storedHash The stored "salt:hash" string.
     * @return true if the passwords match, false otherwise.
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null || !storedHash.contains(":")) {
            return false;
        }

        try {
            String[] parts = storedHash.split(":");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedPasswordHash = Base64.getDecoder().decode(parts[1]);
            byte[] hashedPasswordAttempt = hashWithSalt(plainPassword.getBytes(), salt);

            return MessageDigest.isEqual(hashedPasswordAttempt, storedPasswordHash);
        } catch (Exception e) {
            // If decoding fails, it's not a valid format
            return false;
        }
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] hashWithSalt(byte[] password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            return md.digest(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found.", e);
        }
    }
}
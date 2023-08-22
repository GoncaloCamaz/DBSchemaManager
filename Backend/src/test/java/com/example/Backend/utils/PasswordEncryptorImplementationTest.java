package com.example.Backend.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncryptorImplementationTest {

    @Test
    void encryptPassword() {
        PasswordEncryptor encryptor = new PasswordEncryptorImplementation();
        String password = "efrdae-13259P_29381B5";
        String encrypted = encryptor.encryptPassword(password);
        assertNotEquals(encrypted, encryptor.encryptPassword(password));
    }

    @Test
    void checkPassword() {
        PasswordEncryptor encryptor = new PasswordEncryptorImplementation();
        String password = "efrdae-13259P_29381B5";
        String encrypted = encryptor.encryptPassword(password);
        assertTrue(encryptor.checkPassword(password, encrypted));
    }
}
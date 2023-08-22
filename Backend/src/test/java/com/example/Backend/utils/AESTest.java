package com.example.Backend.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AESTest {

    @Test
    void setKey() {
        byte[] key = "testkey123WITHSTRANGECHARACTERSlike#$%!()/".getBytes();
        AES securityAES = new AES();
        securityAES.setKey(key);
        assertEquals(key,securityAES.getKey());
    }

    @Test
    void encrypt() {
        byte[] key = "testkey123WITHSTRANGECHARACTERSlike#$%!()/".getBytes();
        AES securityAES = new AES();
        String password = "passwordToEncrypt123!";
        String encrypted = securityAES.encrypt(password, key.toString());
        assertEquals(encrypted, securityAES.encrypt(password, key.toString()));
    }

    @Test
    void decrypt() {
        byte[] key = "testkey123WITHSTRANGECHARACTERSlike#$%!()/".getBytes();
        AES securityAES = new AES();
        String password = "passwordToEncrypt123!";
        String encrypted = securityAES.encrypt(password, key.toString());
        String decrypted = securityAES.decrypt(encrypted, key.toString());
        assertEquals(password, decrypted);
    }
}
package com.example.Backend.utils;

public interface PasswordEncryptor
{
    String encryptPassword(String password);

    boolean checkPassword(String providedPassword, String encryptedPassword);

    String encryptDataBaseObjectPassword(String password);

    String decryptDataBaseObjectPassword(String password);

    boolean isDataBaseObjectPasswordValid(String providedPassword, String databaseStoredPassword);

    boolean isDatabaseObjectPasswordEncrypted(String providedPassword, String databaseStoredPassword);
}

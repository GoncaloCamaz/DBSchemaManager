package com.example.Backend.utils;

import com.example.Backend.Security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryptorImplementation implements PasswordEncryptor
{
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PasswordEncryptorImplementation()
    {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public String encryptPassword(String password)
    {
        return bCryptPasswordEncoder.encode(password);
    }

    public boolean checkPassword(String providedPassword, String actualEncryptedPassword)
    {
        return bCryptPasswordEncoder.matches(providedPassword, actualEncryptedPassword);
    }

    @Override
    public String encryptDataBaseObjectPassword(String password) {
        return AES.encrypt(password, SecurityConstants.SECRET);
    }

    @Override
    public String decryptDataBaseObjectPassword(String encryptedPassword) {
        return AES.decrypt(encryptedPassword, SecurityConstants.SECRET);
    }

    @Override
    public boolean isDataBaseObjectPasswordValid(String providedPassword, String databaseStoredPassword) {
        if(providedPassword.equals(databaseStoredPassword))
        {
            return true;
        }
        else return AES.decrypt(databaseStoredPassword, SecurityConstants.SECRET).equals(providedPassword);
    }

    @Override
    public boolean isDatabaseObjectPasswordEncrypted(String providedPassword, String databaseStoredPassword) {
        return providedPassword.equals(databaseStoredPassword);
    }
}

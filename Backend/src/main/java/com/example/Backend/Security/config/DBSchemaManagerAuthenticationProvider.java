package com.example.Backend.Security.config;

import com.example.Backend.dto.model.DBSchemaManagerUserDTO;
import com.example.Backend.model.DBSchemaManagerRole;
import com.example.Backend.service.DBSchemaManagerUserService;
import com.example.Backend.utils.PasswordEncryptor;
import com.example.Backend.utils.PasswordEncryptorImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * CLass responsible to establish connection with server and authenticate user
 */
@Component
public class DBSchemaManagerAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private DBSchemaManagerUserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) {

        boolean authenticate;
        PasswordEncryptor encryptor = new PasswordEncryptorImplementation();
        DBSchemaManagerUserDTO fetcher = this.userService.findByUsername(authentication.getName());
        authenticate = encryptor.checkPassword(authentication.getCredentials().toString(),fetcher.getPassword());

        if (authenticate) {
            DBSchemaManagerUserDTO user = userService.findByUsername(authentication.getName());
            DBSchemaManagerRole role = userService.findDBSchemaManagerRoleByName(user.getRole());
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            UserDetails userDetails = new User(authentication.getName(), authentication.getCredentials().toString()
                    , grantedAuthorities);

            return new UsernamePasswordAuthenticationToken(userDetails,
                    authentication.getCredentials().toString(), grantedAuthorities);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}


package com.example.Backend.service.implementation;

import com.example.Backend.dto.model.DBSchemaManagerUserDTO;
import com.example.Backend.service.DBSchemaManagerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class DBSchemaManagerUserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private DBSchemaManagerUserService DBSchemaManagerUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        DBSchemaManagerUserDTO user = DBSchemaManagerUserService.findByUsername(username);
        if(user == null)
        {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return new User(user.getUsername(),"", authorities);
    }
}

package com.zmarket.userservice.configs.security;

import com.zmarket.userservice.modules.security.UserModelDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RequiredArgsConstructor
public class DBAuthenticationProvider implements AuthenticationProvider {

    private final UserModelDetailsService userModelDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String name = authentication.getName();
            String pass = authentication.getCredentials().toString();

            UserDetails user = userModelDetailsService.loadUserByUsername(name);

            if (new BCryptPasswordEncoder().matches(pass, user.getPassword())) {
                return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            }

            throw new BadCredentialsException("Invalid username or password");
        }catch (UsernameNotFoundException usernameNotFoundException){
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

}


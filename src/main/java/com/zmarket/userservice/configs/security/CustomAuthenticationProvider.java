package com.zmarket.userservice.configs.security;

import com.zmarket.userservice.exceptions.BadRequestException;
import com.zmarket.userservice.modules.security.dto.ThirdPartyUser;
import com.zmarket.userservice.modules.security.enums.AuthProvider;
import com.zmarket.userservice.modules.security.model.User;
import com.zmarket.userservice.modules.security.repository.UserRepository;
import com.zmarket.userservice.modules.security.service.impl.FacebookClient;
import com.zmarket.userservice.modules.security.service.impl.GoogleClient;
import com.zmarket.userservice.util.CustomAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    private final FacebookClient facebookClient;

    private final GoogleClient googleClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthenticationToken authenticationToken = (CustomAuthenticationToken) authentication;
        String principal = authenticationToken.getCredentials().toString();
        if (authenticationToken.getAuthProvider() == AuthProvider.local) {
            return userEmailAuthentication(principal);
        }

        if (authenticationToken.getAuthProvider() == AuthProvider.google) {
            return userGoogleTokenAuthentication(principal);
        }

        if (authenticationToken.getAuthProvider() == AuthProvider.facebook) {
            return userFacebookTokenAuthentication(principal);
        }

        throw new BadCredentialsException("Authentocation failed");

    }

    private Authentication userEmailAuthentication(String principal) {
        User user = userRepository.findFirstByEmail(principal).orElseThrow(() -> new BadRequestException("Authentication failed"));
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(principal, "", grantedAuthorities);
    }

    private Authentication userGoogleTokenAuthentication(String principal) {
        return getThirdPartyAuthentication(principal, googleClient.getThirdPartyUser(principal));
    }

    private Authentication userFacebookTokenAuthentication(String principal) {
        return getThirdPartyAuthentication(principal, facebookClient.getThirdPartyUser(principal));
    }


    private Authentication getThirdPartyAuthentication(String principal, ThirdPartyUser thirdPartyUser) {
        User user = userRepository.findFirstByEmail(thirdPartyUser.getEmail()).orElseThrow(() -> new BadRequestException("Authentication failed"));
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(principal, "", grantedAuthorities);
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(CustomAuthenticationToken.class);
    }
}


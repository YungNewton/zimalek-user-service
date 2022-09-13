package com.zmarket.userservice.modules.security.service.impl;

import com.zmarket.userservice.modules.security.dto.GoogleUser;
import com.zmarket.userservice.modules.security.dto.ThirdPartyUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GoogleClient {

    private final RestTemplate restTemplate;


    public ThirdPartyUser getThirdPartyUser(String accessToken) {
        GoogleUser googleUser = getUser(accessToken);
        if (Objects.isNull(googleUser)) {
            return null;
        }
        ThirdPartyUser user = new ThirdPartyUser();
        user.setEmail(googleUser.getEmail());
        user.setFirstname(googleUser.getFirstname());
        user.setLastname(googleUser.getLastname());
        return user;
    }

    private GoogleUser getUser(String accessToken) {
        GoogleUser thirdPartyUser = getGoogleUserInfo(accessToken);
        if (thirdPartyUser != null && thirdPartyUser.isVerified()) {

            return thirdPartyUser;
        }
        return null;
    }



    public GoogleUser getGoogleUserInfo(String accessToken) {
        HttpEntity<GoogleUser> httpEntity = new HttpEntity<>(new HttpHeaders());
        try {

            String url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
            return restTemplate.exchange(url, HttpMethod.POST, httpEntity, GoogleUser.class).getBody();

        }catch (HttpClientErrorException ex) {
            return null;
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
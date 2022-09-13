package com.zmarket.userservice.modules.security.service.impl;

import com.zmarket.userservice.modules.security.dto.FacebookUser;
import com.zmarket.userservice.modules.security.dto.ThirdPartyUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FacebookClient {

    private final RestTemplate restTemplate;

    public ThirdPartyUser getThirdPartyUser(String accessToken) {
        FacebookUser facebookUser = getUser(accessToken);
        if (Objects.isNull(facebookUser)) {
            return null;
        }
        ThirdPartyUser user = new ThirdPartyUser();
        user.setEmail(facebookUser.getEmail());
        user.setFirstname(facebookUser.getFirstName());
        user.setLastname(facebookUser.getLastName());
        return user;
    }

    public FacebookUser getUser(String accessToken) {
        var path = "/me?fields={fields}&redirect={redirect}&access_token={access_token}";
        var fields = "email,first_name,last_name,id,gender";
        final Map<String, String> variables = new HashMap<>();
        variables.put("fields", fields);
        variables.put("redirect", "false");
        variables.put("access_token", accessToken);
        String FACEBOOK_GRAPH_API_BASE = "https://graph.facebook.com";
        return restTemplate.getForObject(FACEBOOK_GRAPH_API_BASE + path, FacebookUser.class, variables);
    }
}
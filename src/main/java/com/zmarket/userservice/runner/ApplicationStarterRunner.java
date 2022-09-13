package com.zmarket.userservice.runner;

import com.zmarket.userservice.modules.security.model.Authority;
import com.zmarket.userservice.modules.security.model.AuthorityName;
import com.zmarket.userservice.modules.security.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStarterRunner implements ApplicationRunner {

    private final AuthorityRepository authorityRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        for (AuthorityName authorityName : AuthorityName.values()) {
            Optional<Authority> auth = authorityRepository.findByName(authorityName);
            if (auth.isPresent()) {
                return;
            }
            Authority authority = new Authority();
            authority.setName(authorityName);
            authorityRepository.save(authority);
        }


    }
}

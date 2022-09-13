package com.zmarket.userservice.modules.security.repository;

import com.zmarket.userservice.modules.security.model.Authority;
import com.zmarket.userservice.modules.security.model.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    Optional<Authority> findByName(AuthorityName name);
}

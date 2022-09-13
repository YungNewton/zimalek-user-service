package com.zmarket.userservice.modules.security.repository;

import com.zmarket.userservice.modules.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);

    Optional<User> findFirstByPhone(String phoneNumber);

}

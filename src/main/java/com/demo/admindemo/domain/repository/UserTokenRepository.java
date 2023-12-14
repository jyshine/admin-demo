package com.demo.admindemo.domain.repository;

import com.demo.admindemo.domain.entity.UserToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByUserName(String userName);
}

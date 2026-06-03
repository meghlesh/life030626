package com.cws.cwslife.repository;

import com.cws.cwslife.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetRepository
        extends JpaRepository<PasswordResetToken, Long> {

    //find record from token
    Optional<PasswordResetToken> findByToken(String token);

    // find record from email
    Optional<PasswordResetToken> findByEmail(String email);
}
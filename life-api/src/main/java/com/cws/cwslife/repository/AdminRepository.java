package com.cws.cwslife.repository;

import com.cws.cwslife.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository
        extends JpaRepository<AdminUser, Long> {

    // search user from mail
    // used for login or duplicate check
    Optional<AdminUser> findByEmail(String email);
}
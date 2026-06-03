package com.cws.cwslife.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.cwslife.model.AdminUser;

public interface ChangePasswordRepository
        extends JpaRepository<AdminUser, Long> {

}
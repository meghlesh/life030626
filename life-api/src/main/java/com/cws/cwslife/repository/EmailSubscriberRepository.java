package com.cws.cwslife.repository;


import com.cws.cwslife.model.EmailSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailSubscriberRepository extends JpaRepository<EmailSubscriber, Long> {
    Optional<EmailSubscriber> findByEmail(String email);
    boolean existsByEmail(String email);
}
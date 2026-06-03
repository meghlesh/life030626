package com.cws.cwslife.repository;

import com.cws.cwslife.model.SocialContribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialContributionRepository
        extends JpaRepository<SocialContribution, Long> {

    Optional<SocialContribution> findByTitle(String title);
}
package com.cws.cwslife.repository;

import com.cws.cwslife.model.SocialGallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialGalleryRepository extends JpaRepository<SocialGallery, Long> {

    Optional<SocialGallery> findByTitle(String title);
}
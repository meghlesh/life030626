package com.cws.cwslife.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

import com.cws.cwslife.model.Gallery;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {

    // 🔍 Check duplicate image
    Optional<Gallery> findByImagePath(String imagePath);

    // 📸 Latest images first
    List<Gallery> findAllByOrderByUploadDateDesc();

    // ✅ Optional category filter
    List<Gallery> findByCategoryOrderByUploadDateDesc(String category);
}
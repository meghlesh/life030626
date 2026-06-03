package com.cws.cwslife.model;

import jakarta.persistence.*;

@Entity
@Table(name = "social_gallery")
public class SocialGallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // IMPORTANT: title must be unique for update/delete by title
    @Column(unique = true, nullable = false)
    private String title;

    private String imageUrl;

    // =========================
    // DEFAULT CONSTRUCTOR
    // =========================
    public SocialGallery() {
    }

    // =========================
    // CONSTRUCTOR (title + image)
    // =========================
    public SocialGallery(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    // =========================
    // GETTERS
    // =========================
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // =========================
    // SETTERS
    // =========================
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
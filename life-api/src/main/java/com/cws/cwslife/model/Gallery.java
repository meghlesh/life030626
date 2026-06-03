package com.cws.cwslife.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Image Path
    private String imagePath;

    // ✅ Category
    @Enumerated(EnumType.STRING)
    private GalleryCategory category;

    // ✅ Title
    private String title;

    // ✅ Upload Date
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;

    // ✅ Auto upload date
    @PrePersist
    protected void onCreate() {
        this.uploadDate = new Date();
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public GalleryCategory getCategory() {
        return category;
    }

    public void setCategory(GalleryCategory category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
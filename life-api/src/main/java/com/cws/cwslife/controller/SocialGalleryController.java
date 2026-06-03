package com.cws.cwslife.controller;

import com.cws.cwslife.model.SocialGallery;
import com.cws.cwslife.repository.SocialGalleryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/social")
@CrossOrigin("*")
public class SocialGalleryController {

    @Autowired
    private SocialGalleryRepository repository;

    private final String UPLOAD_DIR =
            System.getProperty("user.dir") + "/uploads/";

    // =========================
    // ADD IMAGE (CREATE)
    // =========================
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("title") String title,
            @RequestParam("image") MultipartFile file
    ) throws IOException {

        File folder = new File(UPLOAD_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = UPLOAD_DIR + fileName;

        file.transferTo(new File(filePath));

        SocialGallery gallery = new SocialGallery();
        gallery.setTitle(title);
        gallery.setImageUrl("/uploads/" + fileName);

        repository.save(gallery);

        return ResponseEntity.ok("Image Uploaded Successfully");
    }

    // =========================
    // GET ALL IMAGES
    // =========================
    @GetMapping("/getAll")
    public List<SocialGallery> getAllImages() {
        return repository.findAll();
    }

    // =========================
    // UPDATE BY TITLE
    // =========================
    @PutMapping("/updateByTitle")
    public ResponseEntity<?> updateByTitle(
            @RequestParam("title") String title,
            @RequestParam("image") MultipartFile file
    ) throws IOException {

        SocialGallery gallery = repository.findByTitle(title).orElse(null);

        if (gallery == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Title not found");
        }

        // delete old image file
        String oldPath = System.getProperty("user.dir") + gallery.getImageUrl();
        File oldFile = new File(oldPath);
        if (oldFile.exists()) {
            oldFile.delete();
        }

        // upload new image
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = UPLOAD_DIR + fileName;

        file.transferTo(new File(filePath));

        gallery.setImageUrl("/uploads/" + fileName);

        repository.save(gallery);

        return ResponseEntity.ok("Updated Successfully");
    }

    // =========================
    // DELETE BY TITLE
    // =========================
    @DeleteMapping("/deleteByTitle/{title}")
    public ResponseEntity<?> deleteByTitle(@PathVariable String title) {

        SocialGallery gallery = repository.findByTitle(title).orElse(null);

        if (gallery == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Title not found");
        }

        // delete file from folder
        String path = System.getProperty("user.dir") + gallery.getImageUrl();
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        repository.delete(gallery);

        return ResponseEntity.ok("Deleted Successfully");
    }
}
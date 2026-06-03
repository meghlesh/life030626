package com.cws.cwslife.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    // application.properties se upload path lo
    @Value("${app.upload.dir}")
    private String uploadDir;

    // POST /api/upload/image
    // Frontend se image aati hai
    // Server pe save hoti hai
    // URL return hota hai
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file) {

        // File empty check
        if (file.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Please select a file");
            return ResponseEntity.badRequest().body(error);
        }

        // File type check — sirf images allow
        String contentType = file.getContentType();
        if (contentType == null ||
            !contentType.startsWith("image/")) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Only image files are allowed");
            return ResponseEntity.badRequest().body(error);
        }

        try {
            // Upload folder banao agar nahi hai
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Unique file name banao
            String originalName = file.getOriginalFilename();
            String extension    = originalName.substring(
                originalName.lastIndexOf('.'));
            String fileName     = UUID.randomUUID().toString() + extension;

            // File save karo
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath,
                StandardCopyOption.REPLACE_EXISTING);

            // URL return karo
            String fileUrl = "/uploads/events/" + fileName;

            Map<String, String> response = new HashMap<>();
            response.put("message", "Image uploaded successfully!");
            response.put("url",     fileUrl);
            response.put("fileName", fileName);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to upload image: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
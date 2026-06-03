package com.cws.cwslife.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cws.cwslife.model.Gallery;
import com.cws.cwslife.model.GalleryCategory;
import com.cws.cwslife.repository.GalleryRepository;

@RestController
@RequestMapping("/api/gallery")
@CrossOrigin(origins = "*")
public class GalleryController {

    @Autowired
    private GalleryRepository galleryRepository;

    // ✅ ADD IMAGE
    @PostMapping("/add")
    public ResponseEntity<?> addImage(

            @RequestParam("image") MultipartFile file,

            @RequestParam("category") GalleryCategory category,

            @RequestParam("title") String title
    ) {
    	
        try {

            // ✅ Validation
            if (file == null || file.isEmpty()) {

                return ResponseEntity.badRequest()
                        .body("File is required ❌");
            }

            // ✅ File type
            String contentType = file.getContentType();

            boolean isImage =
                    contentType.equals("image/jpeg") ||
                    contentType.equals("image/jpg")  ||
                    contentType.equals("image/png");

            boolean isVideo =
                    contentType.equals("video/mp4") ||
                    contentType.equals("video/quicktime");

            // ✅ CWS_VIDEO category → only video
            if (category == GalleryCategory.CWS_VIDEO) {

                if (!isVideo) {

                    return ResponseEntity.badRequest()
                            .body("Only videos allowed in CWS_VIDEO ❌");
                }

                // ✅ Video max 50MB
                if (file.getSize() > 50 * 1024 * 1024) {

                    return ResponseEntity.badRequest()
                            .body("Video max 50MB is allowed ❌");
                }

            } else {

                // ✅ Other categories → only image
                if (!isImage) {

                    return ResponseEntity.badRequest()
                            .body("Only JPG/JPEG/PNG allowed ❌");
                }

                // ✅ Image max 5MB
                if (file.getSize() > 5 * 1024 * 1024) {

                    return ResponseEntity.badRequest()
                            .body("Image max 5MB is allowed ❌");
                }
            }

            // 📁 Upload folder
            String uploadDir =
                    System.getProperty("user.dir")
                    + File.separator
                    + "gallery";

            File dir = new File(uploadDir);
           
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 📄 Unique filename
            String fileName =
                    System.currentTimeMillis()
                    + "_"
                    + file.getOriginalFilename();

            // 📂 Full path
            Path filePath = Paths.get(uploadDir, fileName);

            // 💾 Save file
            file.transferTo(filePath.toFile());

            // 💾 Save in DB
            Gallery g = new Gallery();

            g.setImagePath("/gallery/" + fileName);

            g.setCategory(category);

            g.setTitle(title);

            galleryRepository.save(g);

            // ✅ Success message
            if (category == GalleryCategory.CWS_VIDEO) {

                return ResponseEntity.ok(
                        "Video uploaded successfully ✅");

            } else {

                return ResponseEntity.ok(
                        "Image uploaded successfully ✅");
            }

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body("Error uploading image ❌");
        }
    }

    // ✅ DELETE IMAGE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Long id) {

        Optional<Gallery> optional =
                galleryRepository.findById(id);

        if (optional.isEmpty()) {

            return ResponseEntity.badRequest()
                    .body("Image not found ❌");
        }

        Gallery gallery = optional.get();

        // 🔥 Delete file from folder
        String imagePath = gallery.getImagePath();

        if (imagePath == null) {

            return ResponseEntity.badRequest()
                    .body("Image path missing ❌");
        }

        String fileName =
                imagePath.replace("/gallery/", "");

        String filePath =
                System.getProperty("user.dir")
                + File.separator
                + "gallery"
                + File.separator
                + fileName;

        File file = new File(filePath);

        if (file.exists()) {
            file.delete();
        }

        // 🔥 Delete from DB
        galleryRepository.deleteById(id);

        return ResponseEntity.ok(
                "Image deleted successfully ✅");
    }

    // ✅ GET ALL IMAGES
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok(
                galleryRepository.findAllByOrderByUploadDateDesc()
        );
    }
}
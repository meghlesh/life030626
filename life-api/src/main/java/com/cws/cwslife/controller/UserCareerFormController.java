package com.cws.cwslife.controller;
import com.cws.cwslife.service.EmailService;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.CrossOrigin;
import com.cws.cwslife.model.Career;
import com.cws.cwslife.repository.CareerRepository;

import com.cws.cwslife.model.UserCareerForm;

import com.cws.cwslife.repository.UserCareerFormRepository;
import com.cws.cwslife.service.EmailService;

@RestController
@RequestMapping("/api/apply")
@CrossOrigin(origins = "*")
public class UserCareerFormController {

    @Autowired
    private UserCareerFormRepository repository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private CareerRepository careerRepository;
   
    @PostMapping("/submit")
    public ResponseEntity<?> applyJob(
    		@RequestParam("jobId") Long jobId, 
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("message") String message,
            @RequestParam("resume") MultipartFile resumeFile
            
    ) {

        try {
        	System.out.println("JOB ID RECEIVED: " + jobId);
            System.out.println("EMAIL: " + email);

//            // ✅ CHECK USER REGISTERED OR NOT
//            if (adminRepository.findByEmail(email).isEmpty()) {
//                return ResponseEntity
//                        .badRequest()
//                        .body("Your Email Id is not registered, Please register first ❌");
//            }
//            
//         // ✅ CHECK USER LOGIN OR NOT
//            String loggedInUser =
//                    (String) session.getAttribute("loggedInUser");
//
//            System.out.println("SESSION USER: " + loggedInUser);
//            System.out.println("FORM EMAIL: " + email);
//
//            if (loggedInUser == null ||
//                    !loggedInUser.equalsIgnoreCase(email)) {
//
//                return ResponseEntity
//                        .badRequest()
//                        .body("Please login first to apply for this job ❌");
//            }

            // ✅ CHECK ALREADY APPLIED
            boolean alreadyApplied =
                    repository.existsByJobIdAndEmail(jobId, email);

            if (alreadyApplied) {
                return ResponseEntity
                        .badRequest()
                        .body("You already applied for this job ❌");
            }

            // ✅ Resume Validation
            if (resumeFile == null || resumeFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Resume file is required ❌");
            }

            if (!"application/pdf".equals(resumeFile.getContentType())) {
                return ResponseEntity.badRequest().body("Only PDF files are allowed ❌");
            }

            if (resumeFile.getSize() > 2 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("File size must be less than 2MB ❌");
            }

            // 📁 Upload Path
            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 📄 Unique File Name
            String fileName = System.currentTimeMillis() + "_" + resumeFile.getOriginalFilename();

            Path filePath = Paths.get(uploadDir, fileName);

            // 💾 Save File
            resumeFile.transferTo(filePath.toFile());

            // 💾 Save Data in DB
            UserCareerForm user = new UserCareerForm();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setMessage(message);
            user.setResumeFileName(fileName);
            user.setJobId(jobId);
            
            repository.save(user);
           
            //Mail Send to user
            Career career = careerRepository.findById(jobId).orElse(null);
            String jobTitle = "Job Application";
            if (career != null) {
                jobTitle = career.getTitle();
            }

            // USER CONFIRMATION MAIL SEND
            emailService.sendApplicationMail(
                    email,
                    firstName + " " + lastName,
                    jobTitle
            );
            
         // FULL RESUME PATH
            String fullResumePath = filePath.toString();
            
         // ADMIN NOTIFICATION MAIL
            emailService.sendAdminNotification(
                    firstName + " " + lastName,
                    email,
                    phone,
                    jobTitle,
                    message,
                    fullResumePath
            );


            
            return ResponseEntity.ok("Application Submitted Successfully ✅");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error uploading file ❌");
        }
    }
}
package com.cws.cwslife.controller;

import com.cws.cwslife.model.AdminUser;
import jakarta.servlet.http.HttpSession;
import com.cws.cwslife.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AdminRepository adminRepository;

    // ── REGISTER ──
    // POST /api/auth/register
    
    // Fields: fullName, email, phone, password, confirmPassword
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Map<String, String> request) {

        String fullName        = request.get("fullName");
        String email           = request.get("email");
        String phone           = request.get("phone");
        String password        = request.get("password");
        String confirmPassword = request.get("confirmPassword");

        // Required fields check
        if (fullName == null || fullName.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Full name is required");
            return ResponseEntity.badRequest().body(error);
        }

        if (email == null || email.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Email is required");
            return ResponseEntity.badRequest().body(error);
        }

        if (phone == null || phone.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Phone number is required");
            return ResponseEntity.badRequest().body(error);
        }

        if (password == null || password.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Password is required");
            return ResponseEntity.badRequest().body(error);
        }

        // Password and confirm password match check
        if (!password.equals(confirmPassword)) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Password and confirm password do not match");
            return ResponseEntity.badRequest().body(error);
        }

        // Password minimum 6 characters check
        if (password.length() < 6) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Password must be at least 6 characters");
            return ResponseEntity.badRequest().body(error);
        }

        // Duplicate email check
        if (adminRepository.findByEmail(email).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Email already registered. Please login.");
            return ResponseEntity.badRequest().body(error);
        }

        //  
        AdminUser user = new AdminUser();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setRole("USER");

        // Database save
        adminRepository.save(user);

        // Success response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Account created successfully!");
        response.put("name",    fullName);
        response.put("email",   email);

        return ResponseEntity.ok(response);
    }

    // ── LOGIN ──
    // POST /api/auth/login
    // work for both admin and user
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request,
            HttpSession session) {

        String email    = request.get("email");
        String password = request.get("password");

        // find user using email
        Optional<AdminUser> userOpt =
            adminRepository.findByEmail(email);

        // Email and password check 
        if (userOpt.isPresent() &&
            userOpt.get().getPassword().equals(password)) {

            AdminUser user = userOpt.get();
            
         // ✅ SESSION CREATE
            session.setAttribute(
                    "loggedInUser",
                    user.getEmail()
            );

            System.out.println(
                    "LOGIN SESSION = "
                    + session.getAttribute("loggedInUser")
            );

            // Success response
            Map<String, String> response = new HashMap<>();
            response.put("token", "token-" + user.getId());
            response.put("name",  user.getFullName());
            response.put("email", user.getEmail());
            response.put("role",  user.getRole());

            return ResponseEntity.ok(response);
        }

        // Wrong credentials
        Map<String, String> error = new HashMap<>();
        error.put("message", "Invalid email or password");
        return ResponseEntity.status(401).body(error);
    }

    // ── GET ALL USERS ──
    // GET /api/auth/users
    // Admin can see all user
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(adminRepository.findAll());
    }
}
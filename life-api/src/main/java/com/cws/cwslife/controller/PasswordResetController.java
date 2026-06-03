package com.cws.cwslife.controller;

import com.cws.cwslife.model.AdminUser;
import com.cws.cwslife.model.PasswordResetToken;
import com.cws.cwslife.repository.AdminRepository;
import com.cws.cwslife.repository.PasswordResetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class PasswordResetController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private JavaMailSender mailSender;

    // Frontend URL — application.properties se aata hai
    @Value("${app.frontend.url}")
    private String frontendUrl;

    // ── FORGOT PASSWORD ──
    // POST /api/auth/forgot-password
    // User email daalta hai
    // Reset link email pe jaata hai
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody Map<String, String> request) {

        String email = request.get("email");

        // Email check
        if (email == null || email.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Email is required");
            return ResponseEntity.badRequest().body(error);
        }

        // Check karo email database mein hai ya nahi
        Optional<AdminUser> userOpt =
            adminRepository.findByEmail(email);

        if (!userOpt.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Email not found");
            return ResponseEntity.badRequest().body(error);
        }

        // Unique token generate 
        String token = UUID.randomUUID().toString();

        // Token 30 minutes ke liye valid rahega
        LocalDateTime expiresAt =
            LocalDateTime.now().plusMinutes(30);

        // delete ol token
        passwordResetRepository.findByEmail(email)
            .ifPresent(t -> passwordResetRepository.delete(t));

        // save new token in database
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiresAt(expiresAt);
        passwordResetRepository.save(resetToken);

        // Reset link banao
        String resetLink = frontendUrl +
            "/reset-password.html?token=" + token;

        // Email bhejo
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("CWS.life — Password Reset Request");
        message.setText(
            "Hello " + userOpt.get().getFullName() + ",\n\n" +
            "We received a request to reset your password.\n\n" +
            "Click the link below to reset your password:\n" +
            resetLink + "\n\n" +
            "This link will expire in 30 minutes.\n\n" +
            "If you did not request this, " +
            "please ignore this email.\n\n" +
            "Regards,\n" +
            "CWS.life Team"
        );

        mailSender.send(message);

        // Success response
        Map<String, String> response = new HashMap<>();
        response.put("message",
            "Password reset link sent to " + email);
        return ResponseEntity.ok(response);
    }

    // ── RESET PASSWORD ──
    // POST /api/auth/reset-password
    // User new password daalta hai
    // Database mein update hota hai
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody Map<String, String> request) {

        String token           = request.get("token");
        String newPassword     = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");

        // Fields check
        if (token == null || token.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Token is required");
            return ResponseEntity.badRequest().body(error);
        }

        if (newPassword == null || newPassword.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "New password is required");
            return ResponseEntity.badRequest().body(error);
        }

        // Password match check
        if (!newPassword.equals(confirmPassword)) {
            Map<String, String> error = new HashMap<>();
            error.put("message",
                "Password and confirm password do not match");
            return ResponseEntity.badRequest().body(error);
        }

        // Minimum 6 characters check
        if (newPassword.length() < 6) {
            Map<String, String> error = new HashMap<>();
            error.put("message",
                "Password must be at least 6 characters");
            return ResponseEntity.badRequest().body(error);
        }

        // check in Token database
        Optional<PasswordResetToken> tokenOpt =
            passwordResetRepository.findByToken(token);

        // could not find token
        if (!tokenOpt.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid or expired token");
            return ResponseEntity.badRequest().body(error);
        }

        PasswordResetToken resetToken = tokenOpt.get();

        // Token already used
        if (resetToken.getIsUsed()) {
            Map<String, String> error = new HashMap<>();
            error.put("message",
                "This link has already been used");
            return ResponseEntity.badRequest().body(error);
        }

        // Token expire ho gaya
        if (resetToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {
            Map<String, String> error = new HashMap<>();
            error.put("message",
                "Link has expired. Please request a new one.");
            return ResponseEntity.badRequest().body(error);
        }

        // find user and update password
        Optional<AdminUser> userOpt =
            adminRepository.findByEmail(resetToken.getEmail());

        if (!userOpt.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "User not found");
            return ResponseEntity.badRequest().body(error);
        }

        // Password update 
        AdminUser user = userOpt.get();
        user.setPassword(newPassword);
        adminRepository.save(user);

        // Token used mark 
        resetToken.setIsUsed(true);
        passwordResetRepository.save(resetToken);

        // Success response
        Map<String, String> response = new HashMap<>();
        response.put("message",
            "Password changed successfully!");
        return ResponseEntity.ok(response);
    }
}
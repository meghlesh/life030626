package com.cws.cwslife.controller;

import com.cws.cwslife.model.ContactInquiry;

import com.cws.cwslife.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import com.cws.cwslife.service.EmailService;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private EmailService emailService;

    // reCAPTCHA Secret Key
    private static final String SECRET_KEY =
        "6LfjC_8sAAAAACpuJ6KNb7jUZUT4KchWFH8i8UzC";

    // reCAPTCHA verify URL
    private static final String VERIFY_URL =
        "https://www.google.com/recaptcha/api/siteverify";

    // ── Verify reCAPTCHA Token ──
    private boolean verifyCaptcha(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = VERIFY_URL +
                "?secret=" + SECRET_KEY +
                "&response=" + token;
            Map response = restTemplate.postForObject(
                url, null, Map.class);
            return response != null &&
                Boolean.TRUE.equals(response.get("success"));
        } catch (Exception e) {
            return false;
        }
    }

    // ── SUBMIT CONTACT FORM ──
    @PostMapping("/submit")
    public ResponseEntity<?> submitContact(
            @RequestBody Map<String, String> request) {

        String firstName    = request.get("firstName");
        String lastName     = request.get("lastName");
        String email        = request.get("email");
        String phone        = request.get("phone");
        String message      = request.get("message");
        String captchaToken = request.get("captchaToken");

        // Required fields check
        if (firstName == null || email == null || message == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Please fill all required fields");
            return ResponseEntity.badRequest().body(error);
        }

        // reCAPTCHA verify 
        if (!verifyCaptcha(captchaToken)) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Captcha verification failed. Please try again.");
            return ResponseEntity.badRequest().body(error);
        }

        // create object
        ContactInquiry inquiry = new ContactInquiry();
        inquiry.setFirstName(firstName);
        inquiry.setLastName(lastName);
        inquiry.setEmail(email);
        inquiry.setPhone(phone);
        inquiry.setMessage(message);

        // Saved in database
        contactRepository.save(inquiry);
        // Send mail to admin
        emailService.sendContactNotification(
            firstName, lastName, email, phone, message
        );
        // Success response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Message sent successfully!");
        return ResponseEntity.ok(response);
    }
    
 // ── GET LATEST NOTIFICATIONS ──
    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications() {
        List<ContactInquiry> latest = contactRepository.findTop5ByOrderByCreatedAtDesc();
        List<Map<String, String>> result = new ArrayList<>();
        for (ContactInquiry c : latest) {
            Map<String, String> item = new HashMap<>();
            item.put("name", c.getFirstName() + " " + c.getLastName());
            item.put("message", c.getMessage().length() > 50
                ? c.getMessage().substring(0, 50) + "..."
                : c.getMessage());
            item.put("time", c.getCreatedAt().toString().substring(0, 16).replace("T", " "));
            result.add(item);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("count", contactRepository.count());
        response.put("notifications", result);
        return ResponseEntity.ok(response);
    }

    // ── GET ALL INQUIRIES ──
    @GetMapping("/all")
    public ResponseEntity<?> getAllInquiries() {
        return ResponseEntity.ok(contactRepository.findAll());
    }
	 // ── DELETE CONTACT ──
	 // DELETE /api/contact/delete/{id}
	 @DeleteMapping("/delete/{id}")
	 public ResponseEntity<?> deleteContact(@PathVariable Long id) {
	     if (!contactRepository.existsById(id)) {
	         Map<String, String> error = new HashMap<>();
	         error.put("message", "Contact not found");
	         return ResponseEntity.status(404).body(error);
	     }
	     contactRepository.deleteById(id);
	     Map<String, String> response = new HashMap<>();
	     response.put("message", "Contact deleted successfully!");
	     return ResponseEntity.ok(response);
	 }
}
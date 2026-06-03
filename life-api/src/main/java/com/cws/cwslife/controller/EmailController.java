package com.cws.cwslife.controller;

//import com.cws.cwslife.newsletter.entity.EmailLog;
import com.cws.cwslife.model.EmailLog;
//import com.cws.cwslife.newsletter.entity.EmailSubscriber;
import com.cws.cwslife.model.EmailSubscriber;
import com.cws.cwslife.service.EmailServiceNewsletter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = {
    "http://localhost:5500",
    "http://127.0.0.1:5500", 
    "http://localhost:5501",
    "http://127.0.0.1:5501",
    "http://localhost:8080"
})
public class EmailController {
    
    @Autowired
    private EmailServiceNewsletter emailService;
    
    // Send single email
    @PostMapping("/send")
    public String sendEmail(@RequestBody Map<String, String> request) {
        String to = request.get("to");
        String subject = request.get("subject");
        String message = request.get("message");
        return emailService.sendEmail(to, subject, message);
    }
    
    // Add subscriber
    @PostMapping("/subscribe")
    public String subscribe(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return emailService.addSubscriber(email);
    }
    
    // Send newsletter to all subscribers
    @PostMapping("/send-newsletter")
    public String sendNewsletter(@RequestBody Map<String, String> request) {
        String subject = request.get("subject");
        String message = request.get("message");
        return emailService.sendNewsletterToAll(subject, message);
    }
    
    // Get all subscribers
    @GetMapping("/subscribers")
    public List<EmailSubscriber> getSubscribers() {
        return emailService.getAllSubscribers();
    }
    
    // Get all email logs
    @GetMapping("/logs")
    public List<EmailLog> getLogs() {
        return emailService.getAllLogs();
    }
    
    // Simple test endpoint
    @GetMapping("/test")
    public String test() {
        return "Email API is working!";
    }
    
    // Test email endpoint
    @GetMapping("/test-email")
    public String testEmail(@RequestParam String to) {
        return emailService.sendEmail(to, "Test Email", "This is a test email from your backend!");
    }
}
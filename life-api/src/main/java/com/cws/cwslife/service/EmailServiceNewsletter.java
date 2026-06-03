package com.cws.cwslife.service;

import com.cws.cwslife.model.EmailLog;
import com.cws.cwslife.model.EmailSubscriber;
import com.cws.cwslife.repository.EmailLogRepository;
import com.cws.cwslife.repository.EmailSubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.io.File;
import java.io.InputStream;

@Service
public class EmailServiceNewsletter {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceNewsletter.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private EmailSubscriberRepository subscriberRepository;
    
    @Autowired
    private EmailLogRepository emailLogRepository;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    // Make image path configurable via properties
    @Value("${email.logo.white.path:}")
    private String whiteLogoPath;
    
    @Value("${email.logo.default.path:}")
    private String defaultLogoPath;
    
    public String addSubscriber(String email) {
        if (subscriberRepository.existsByEmail(email)) {
            return "❌ Email already subscribed!";
        }
        
        EmailSubscriber subscriber = new EmailSubscriber();
        subscriber.setEmail(email);
        subscriber.setSubscribedDate(LocalDateTime.now());
        subscriberRepository.save(subscriber);
        
        sendConfirmationEmail(email);
        
        return "✅ Subscribed successfully! Confirmation email sent to " + email;
    }
    
    private void sendConfirmationEmail(String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setTo(email);
            helper.setSubject("✅ Welcome to CreativeWebSolution!");
            helper.setFrom(fromEmail);
            
            String htmlContent = getConfirmationEmailTemplate();
            helper.setText(htmlContent, true);
            
            // Add logo as inline attachment - IMPROVED VERSION
            boolean logoAdded = addLogoToEmail(helper);
            
            if (!logoAdded) {
                logger.warn("No logo was added to the email for: {}", email);
                // Optional: Send without logo (template has fallback text)
            }
            
            mailSender.send(mimeMessage);
            logger.info("Confirmation email sent successfully to: {}", email);
            
        } catch (MessagingException e) {
            logger.error("Failed to send confirmation email to: {}", email, e);
        }
    }
    
    private boolean addLogoToEmail(MimeMessageHelper helper) throws MessagingException {
        Resource logoResource = null;
        String logoPath = null;
        
        // Strategy 1: Try white logo path from properties
        if (whiteLogoPath != null && !whiteLogoPath.trim().isEmpty()) {
            File logoFile = new File(whiteLogoPath);
            if (logoFile.exists() && logoFile.isFile()) {
                logoResource = new FileSystemResource(logoFile);
                logoPath = whiteLogoPath;
                logger.info("Found logo at configured white logo path: {}", whiteLogoPath);
            } else {
                logger.warn("White logo not found at: {}", whiteLogoPath);
            }
        }
        
        // Strategy 2: Try default logo path from properties
        if (logoResource == null && defaultLogoPath != null && !defaultLogoPath.trim().isEmpty()) {
            File logoFile = new File(defaultLogoPath);
            if (logoFile.exists() && logoFile.isFile()) {
                logoResource = new FileSystemResource(logoFile);
                logoPath = defaultLogoPath;
                logger.info("Found logo at configured default logo path: {}", defaultLogoPath);
            } else {
                logger.warn("Default logo not found at: {}", defaultLogoPath);
            }
        }
        
        // Strategy 3: Try classpath resource (for logo in resources folder)
        if (logoResource == null) {
            try {
                // Try to load from classpath: static/images/ or just images/
                ClassPathResource classpathLogo = new ClassPathResource("static/images/CWS-LOGO1.png");
                if (classpathLogo.exists()) {
                    logoResource = classpathLogo;
                    logoPath = "classpath:static/images/CWS-LOGO1.png";
                    logger.info("Found logo in classpath: static/images/CWS-LOGO1.png");
                } else {
                    // Try alternative path
                    classpathLogo = new ClassPathResource("images/CWS-LOGO1.png");
                    if (classpathLogo.exists()) {
                        logoResource = classpathLogo;
                        logoPath = "classpath:images/CWS-LOGO1.png";
                        logger.info("Found logo in classpath: images/CWS-LOGO1.png");
                    }
                }
            } catch (Exception e) {
                logger.warn("Could not load logo from classpath: {}", e.getMessage());
            }
        }
        
        // Strategy 4: Try relative path from project root
        if (logoResource == null) {
            String[] relativePaths = {
                "src/main/resources/static/images/CWS-LOGO1.png",
                "src/main/resources/images/CWS-LOGO1.png",
                "assets/images/CWS-LOGO1.png",
                "CWS-LOGO1.png"
            };
            
            for (String path : relativePaths) {
                File logoFile = new File(path);
                if (logoFile.exists() && logoFile.isFile()) {
                    logoResource = new FileSystemResource(logoFile);
                    logoPath = path;
                    logger.info("Found logo at relative path: {}", path);
                    break;
                }
            }
        }
        
        // Add logo if found
        if (logoResource != null && logoResource.exists()) {
            try {
                helper.addInline("logoImage", logoResource);
                logger.info("✅ Logo successfully attached from: {}", logoPath);
                return true;
            } catch (Exception e) {
                logger.error("Failed to attach logo from {}: {}", logoPath, e.getMessage());
                return false;
            }
        } else {
            logger.error("❌ LOGO NOT FOUND! Please ensure your logo file exists at one of these locations:");
            logger.error("   1. Configured white logo path: {}", whiteLogoPath);
            logger.error("   2. Configured default logo path: {}", defaultLogoPath);
            logger.error("   3. Classpath: src/main/resources/static/images/CWS-LOGO1.png");
            logger.error("   4. Classpath: src/main/resources/images/CWS-LOGO1.png");
            logger.error("   5. Relative path: assets/images/CWS-LOGO1.png");
            return false;
        }
    }
    
    private String getConfirmationEmailTemplate() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Welcome to CreativeWebSolution</title>
                <style>
                    body {
                        margin: 0;
                        padding: 0;
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background-color: #f4f7fa;
                    }
                    .email-container {
                        max-width: 600px;
                        margin: 0 auto;
                        background-color: #ffffff;
                        border-radius: 12px;
                        overflow: hidden;
                        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
                    }
                    .header {
                        background-color: #0a2540;
                        padding: 40px 20px;
                        text-align: center;
                    }
                    .logo {
                        max-width: 250px;
                        width: auto;
                        height: auto;
                        display: inline-block;
                    }
                    /* Fallback text style (if image doesn't load) */
                    .logo-fallback {
                        display: none;
                    }
                    .content {
                        padding: 40px 30px;
                        background-color: #ffffff;
                    }
                    .greeting {
                        font-size: 24px;
                        color: #333333;
                        margin-bottom: 20px;
                        font-weight: 600;
                    }
                    .message {
                        color: #555555;
                        line-height: 1.6;
                        margin-bottom: 25px;
                        font-size: 16px;
                    }
                    @media only screen and (max-width: 600px) {
                        .content {
                            padding: 20px 15px;
                        }
                        .greeting {
                            font-size: 20px;
                        }
                        .logo {
                            max-width: 180px;
                        }
                    }
                </style>
            </head>
            <body style="margin: 0; padding: 20px; background-color: #f4f7fa;">
                <div class="email-container">
                    <div class="header">
                        <img src="cid:logoImage" alt="CreativeWebSolution" class="logo" border="0" style="max-width:250px; width:auto; height:auto;">
                        <!-- Fallback text that shows only if image doesn't load -->
                        <div class="logo-fallback" style="display:none;">CreativeWebSolution</div>
                    </div>
                    <div class="content">
                        <div class="greeting">Dear Subscriber,</div>
                        <div class="message">
                            Thank you for subscribing to the <strong>CreativeWebSolution</strong>.
                        </div>
                        <div class="message">
                            We're happy to have you with us. You'll now receive the latest updates, news, blogs, and important announcements directly in your inbox.
                        </div>
                        <div class="message" style="margin-top: 30px;">
                            Stay connected and explore more with CreativeWebSolution.
                        </div>
                        <div class="message">
                            Best Regards,<br>
                            <strong>Team CreativeWebSolution</strong>
                        </div>
                    </div>
                </div>
            </body>
            </html>""";
    }
    
    public String sendEmail(String toEmail, String subject, String message) {
        try {
            SimpleMailMessage emailMessage = new SimpleMailMessage();
            emailMessage.setTo(toEmail);
            emailMessage.setSubject(subject);
            emailMessage.setText(message);
            emailMessage.setFrom(fromEmail);
            mailSender.send(emailMessage);
            
            saveEmailLog(toEmail, subject, message, "SUCCESS", null);
            logger.info("Email sent successfully to: {}", toEmail);
            
            return "Email sent successfully to: " + toEmail;
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            saveEmailLog(toEmail, subject, message, "FAILED", errorMsg);
            logger.error("Failed to send email to: {}", toEmail, e);
            return "Failed to send email: " + errorMsg;
        }
    }
    
    private void saveEmailLog(String toEmail, String subject, String message, 
                              String status, String errorMsg) {
        EmailLog log = new EmailLog();
        log.setRecipientEmail(toEmail);
        log.setSubject(subject);
        log.setMessage(message);
        log.setStatus(status + (errorMsg != null ? ": " + errorMsg : ""));
        log.setSentDate(LocalDateTime.now());
        emailLogRepository.save(log);
    }
    
    public String sendNewsletterToAll(String subject, String message) {
        List<EmailSubscriber> subscribers = subscriberRepository.findAll();
        if (subscribers.isEmpty()) {
            return "No subscribers found!";
        }
        
        int successCount = 0;
        int failCount = 0;
        
        for (EmailSubscriber subscriber : subscribers) {
            String result = sendEmail(subscriber.getEmail(), subject, message);
            if (result.contains("successfully")) {
                successCount++;
            } else {
                failCount++;
            }
        }
        
        logger.info("Newsletter sent - Success: {}, Failed: {}", successCount, failCount);
        return "Newsletter sent! Success: " + successCount + ", Failed: " + failCount;
    }
    
    public List<EmailLog> getAllLogs() {
        return emailLogRepository.findAll();
    }
    
    public List<EmailSubscriber> getAllSubscribers() {
        return subscriberRepository.findAll();
    }
}
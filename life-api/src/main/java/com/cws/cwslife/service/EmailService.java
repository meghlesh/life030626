package com.cws.cwslife.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.io.File;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendApplicationMail(
            String toEmail,
            String name,
            String jobTitle
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);

        message.setSubject("Application Received - " + jobTitle);

        message.setText(
                "Dear " + name + ",\n\n" +

                "Thank you for applying for the position of "
                + jobTitle + " at CWS.\n\n" +

                "We have successfully received your application.\n" +
                "Our recruitment team will review your profile and contact you shortly if your qualifications match our requirements.\n\n" +

                "We appreciate your interest in joining CWS.\n\n" +

                "Best Regards,\n" +
                "CWS Recruitment Team"
        );

        mailSender.send(message);
    }
    
    
    
    
    public void sendAdminNotification(
            String candidateName,
            String candidateEmail,
            String phone,
            String jobTitle,
            String messageText,
            String resumePath
    ) {

        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("dipalicws@gmail.com");

            // ADMIN EMAIL
            helper.setTo("dipalicws@gmail.com");

            helper.setSubject("New Job Application - " + jobTitle);

            helper.setText(
                    "A new candidate has applied.\n\n" +

                    "Candidate Name: " + candidateName + "\n" +
                    "Email: " + candidateEmail + "\n" +
                    "Phone: " + phone + "\n" +
                    "Job Title: " + jobTitle + "\n\n" +

                    "Message:\n" + messageText
            );

            // RESUME ATTACHMENT
            FileSystemResource file =
                    new FileSystemResource(new File(resumePath));

            helper.addAttachment(file.getFilename(), file);

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 // ── Contact Form Notification to Admin ──
    public void sendContactNotification(
            String firstName,
            String lastName,
            String email,
            String phone,
            String message
    ) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo("dipali@creativewebsolution.in");
            mail.setSubject("New Contact Inquiry from " + firstName + " " + lastName);
            mail.setText(
                "New message received on CWS.life\n\n" +
                "Name    : " + firstName + " " + lastName + "\n" +
                "Email   : " + email + "\n" +
                "Phone   : " + (phone != null ? phone : "Not provided") + "\n" +
                "Message : " + message + "\n\n" +
                "View in admin panel:\n" +
                "http://127.0.0.1:5501/frontend/admin-contact-us.html"
            );
            mailSender.send(mail);
        } catch (Exception e) {
            System.out.println("Contact email failed: " + e.getMessage());
        }
    }
}
package com.cws.cwslife.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cws.cwslife.model.AdminUser;
import com.cws.cwslife.model.ChangePassword;
import com.cws.cwslife.repository.ChangePasswordRepository;

@RestController
@RequestMapping("/api/password")
public class ChangePasswordController {

    @Autowired
    private ChangePasswordRepository repository;

    @PutMapping("/change")
    public String changePassword(
            @RequestBody ChangePassword request) {

        try {

            System.out.println("========= API HIT =========");

            System.out.println("ID: " + request.getId());

            System.out.println("Current Password: "
                    + request.getCurrentPassword());

            Optional<AdminUser> optionalUser =
                    repository.findById(request.getId());

            if (optionalUser.isEmpty()) {

                System.out.println("USER NOT FOUND");

                return "User not found";
            }

            AdminUser user = optionalUser.get();

            System.out.println("DB Password: "
                    + user.getPassword());

            // Current password check
            if (!user.getPassword().trim()
                    .equals(request.getCurrentPassword().trim())) {

                return "Current password is incorrect";
            }

            // Confirm password check
            if (!request.getNewPassword()
                    .equals(request.getConfirmPassword())) {

                return "Passwords do not match";
            }

            // Update password
            user.setPassword(request.getNewPassword());

            repository.save(user);

            System.out.println("PASSWORD UPDATED");

            return "Password changed successfully";

        } catch (Exception e) {

            e.printStackTrace();

            return "ERROR : " + e.getMessage();
        }
    }
}
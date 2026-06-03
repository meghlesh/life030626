package com.cws.cwslife.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

//  class-users table in database
// Admin and User 
@Entity
@Table(name = "users")
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // full_name column
    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    // phone column
    private String phone;

    // USER ya SUPER_ADMIN
    private String role = "USER";

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters
    public Long getId()                  { return id; }
    public String getFullName()          { return fullName; }
    public String getEmail()             { return email; }
    public String getPassword()          { return password; }
    public String getPhone()             { return phone; }
    public String getRole()              { return role; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    // Setters
    public void setFullName(String v)   { this.fullName = v; }
    public void setEmail(String v)      { this.email = v; }
    public void setPassword(String v)   { this.password = v; }
    public void setPhone(String v)      { this.phone = v; }
    public void setRole(String v)       { this.role = v; }
}
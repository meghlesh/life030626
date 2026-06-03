package com.cws.cwslife.controller;

import com.cws.cwslife.model.Visitor;
import com.cws.cwslife.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/visitors")
@CrossOrigin(origins = {
        "http://127.0.0.1:5501",
        "http://localhost:5501"
})
public class VisitorController {

    @Autowired
    private VisitorRepository visitorRepository;

    // Increment visitor count
    @PostMapping("/increment")
    public String incrementVisitors() {

        Visitor visitor = visitorRepository.findById(1L).orElse(null);

        if (visitor == null) {

            visitor = new Visitor();

            visitor.setTotalVisitors(1);

            // added
            visitor.setCreatedAt(LocalDateTime.now());
            visitor.setUpdatedAt(LocalDateTime.now());

        } else {

            visitor.setTotalVisitors(visitor.getTotalVisitors() + 1);

            // update time
            visitor.setUpdatedAt(LocalDateTime.now());
        }

        visitorRepository.save(visitor);

        return String.valueOf(visitor.getTotalVisitors());
    }

    // Get visitor count
    @GetMapping
    public String getVisitors() {

        Visitor visitor = visitorRepository.findById(1L).orElse(null);

        if (visitor == null) {
            return "0";
        }

        return String.valueOf(visitor.getTotalVisitors());
    }
}
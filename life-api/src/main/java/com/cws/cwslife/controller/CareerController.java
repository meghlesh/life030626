package com.cws.cwslife.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cws.cwslife.model.Career;
import com.cws.cwslife.repository.CareerRepository;

@RestController
@RequestMapping("/api/career")
@CrossOrigin(origins = "*")
public class CareerController {

    @Autowired
    private CareerRepository careerRepository;

    // ✅ ADD
    @PostMapping("/add")
    public ResponseEntity<?> addCareer(@RequestBody Career career) {
        Career savedCareer = careerRepository.save(career);
        return ResponseEntity.ok(savedCareer);
    }

    // ✅ GET ALL
    @GetMapping("/all")
    public ResponseEntity<List<Career>> getAllCareers() {
        return ResponseEntity.ok(careerRepository.findAll());
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCareer(@PathVariable Long id) {
        Optional<Career> career = careerRepository.findById(id);

        if (career.isPresent()) {
            return ResponseEntity.ok(career.get());
        } else {
            return ResponseEntity.status(404).body("Career not found");
        }
    }

    // ✅ UPDATE STATUS
    @PatchMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestBody Map<String, String> statusMap) {

        Optional<Career> optional = careerRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.status(404).body("Career not found");
        }

        Career career = optional.get();
        career.setStatus(statusMap.get("status"));

        return ResponseEntity.ok(careerRepository.save(career));
    }

    // ✅ DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCareer(@PathVariable Long id) {

        if (!careerRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Career not found");
        }

        careerRepository.deleteById(id);
        return ResponseEntity.ok("Career deleted successfully");
    }

    
    //FULL UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCareer(@PathVariable Long id,
                                          @RequestBody Career updatedCareer) {

        Optional<Career> optional = careerRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.status(404).body("Career not found");
        }

        Career career = optional.get();

        // Basic fields
        career.setTitle(updatedCareer.getTitle());
        career.setDepartment(updatedCareer.getDepartment());
        career.setLocation(updatedCareer.getLocation());
        career.setType(updatedCareer.getType());
        career.setExperience(updatedCareer.getExperience());
        career.setOpenings(updatedCareer.getOpenings());
        career.setStatus(updatedCareer.getStatus());

        // ✅ NEW FIELDS (IMPORTANT)
        career.setDescription(updatedCareer.getDescription());
        career.setRoles(updatedCareer.getRoles());
        career.setResponsibilities(updatedCareer.getResponsibilities());
        career.setRequirements(updatedCareer.getRequirements());
        career.setEducation(updatedCareer.getEducation());

        Career saved = careerRepository.save(career);

        return ResponseEntity.ok(saved);
    }

    // ✅ GET ACTIVE
    @GetMapping("/active")
    public ResponseEntity<List<Career>> getActiveCareers() {
        List<Career> active = careerRepository.findByStatusIgnoreCase("ACTIVE");
        return ResponseEntity.ok(active);
    }
    
   // ✅ FILTER API (NEW)
    @GetMapping("/filter")
    public ResponseEntity<List<Career>> filterJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String experience
    ) {

        List<Career> result;

        if (keyword != null && !keyword.isEmpty()
            && type != null && !type.isEmpty()
            && experience != null && !experience.isEmpty()) {

            result = careerRepository
                .findByTitleContainingIgnoreCaseAndTypeIgnoreCaseAndExperienceContainingIgnoreCaseAndStatusIgnoreCase(
                    keyword, type, experience, "ACTIVE");

        } else if (keyword != null && !keyword.isEmpty()) {

            result = careerRepository.universalSearch(keyword);


        } else if (type != null && !type.isEmpty() && experience != null && !experience.isEmpty()) {

            result = careerRepository
                .findByTypeIgnoreCaseAndExperienceContainingIgnoreCaseAndStatusIgnoreCase(type, experience, "ACTIVE");

        } else if (type != null && !type.isEmpty()) {

            result = careerRepository
                .findByTypeIgnoreCaseAndStatusIgnoreCase(type, "ACTIVE");

        } else if (experience != null && !experience.isEmpty()) {

            result = careerRepository
                .findByExperienceContainingIgnoreCaseAndStatusIgnoreCase(experience, "ACTIVE");

        } else {
            result = careerRepository.findByStatusIgnoreCase("ACTIVE");
        }

        return ResponseEntity.ok(result);
    }
}
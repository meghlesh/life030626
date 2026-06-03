package com.cws.cwslife.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cws.cwslife.model.Career;
@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {

    long countByStatus(String status);

    List<Career> findByStatusIgnoreCase(String status);
    
    // 🔍 Keyword search
    List<Career> findByTitleContainingIgnoreCaseAndStatusIgnoreCase(String title, String status);

    // 🔽 Filter by type
    List<Career> findByTypeIgnoreCaseAndStatusIgnoreCase(String type, String status);

    // 🔽 Filter by experience
    List<Career> findByExperienceContainingIgnoreCaseAndStatusIgnoreCase(String experience, String status);

    // 🔽 Filter by type + experience
    List<Career> findByTypeIgnoreCaseAndExperienceContainingIgnoreCaseAndStatusIgnoreCase(
            String type, String experience, String status);

    // 🔥 Full filter (keyword + type + experience)
    List<Career> findByTitleContainingIgnoreCaseAndTypeIgnoreCaseAndExperienceContainingIgnoreCaseAndStatusIgnoreCase(
            String title, String type, String experience, String status);
     
 // 🔥 UNIVERSAL KEYWORD SEARCH (NEW)
    @Query("SELECT c FROM Career c WHERE " +
           "(" +
           "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.type) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.experience) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.roles) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
           ") AND LOWER(c.status) = 'active'")
    List<Career> universalSearch(@Param("keyword") String keyword);
    
    
    default List<Career> findActiveCareers() {
        return findByStatusIgnoreCase("ACTIVE");
    }
}
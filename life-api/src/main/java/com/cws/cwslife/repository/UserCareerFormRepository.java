package com.cws.cwslife.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.cwslife.model.UserCareerForm;

public interface UserCareerFormRepository extends JpaRepository<UserCareerForm, Long>{
	
	 Optional<UserCareerForm> findByEmail(String email);
	 
	 boolean existsByJobIdAndEmail(Long jobId, String email);

}

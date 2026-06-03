package com.cws.cwslife.repository;

import com.cws.cwslife.model.ContactInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;


public interface ContactRepository
        extends JpaRepository<ContactInquiry, Long> {
	List<ContactInquiry> findTop5ByOrderByCreatedAtDesc();

}
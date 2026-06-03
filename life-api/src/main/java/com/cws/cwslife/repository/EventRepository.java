package com.cws.cwslife.repository;

import com.cws.cwslife.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // get event by status
    List<Event> findByStatus(String status);

    // total count by status
    long countByStatus(String status);

    // latest events first
    List<Event> findTop10ByOrderByCreatedAtDesc();
}
package com.BookingService.Repositories;

import org.springframework.data.domain.Pageable; // ✅ correct import
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.BookingService.Entities.Booking;

public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByUserId(String userId);
    Page<Booking> findAll(Pageable pageable);
    

}
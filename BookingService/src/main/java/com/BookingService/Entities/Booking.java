package com.BookingService.Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.BookingService.Enum.BookingStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String bookingId;
    private String userId;
    private String showId;
    private String screenId;
    private LocalDateTime bookingTime;
    private String paymentId;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatus  status;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookedSeat> seats = new ArrayList<>();

    // Getters and Setters
}
package com.BookingService.Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.BookingService.Enum.BookingStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    @GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String bookingId;
    private String userId;
    private String showId;
    private String screenId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.SSS")
    private LocalDateTime bookingTime;
    private String paymentId;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatus  status;
    @JsonBackReference
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookedSeat> seats = new ArrayList<>();

    public Booking(String bookingId, String userId, String showId, String screenId,
            LocalDateTime bookingTime, BookingStatus status, String paymentId,
            double totalAmount, List<BookedSeat> seats) {
 this.bookingId = bookingId;
 this.userId = userId;
 this.showId = showId;
 this.screenId = screenId;
 this.bookingTime = bookingTime;
 this.status = status;
 this.paymentId = paymentId;
 this.totalAmount = totalAmount;
 this.seats = seats;
}

}
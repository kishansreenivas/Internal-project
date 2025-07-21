package com.BookingService.Entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;

import com.BookingService.constants.BookingStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data 
@Entity
@Table(name = "booked_seat")
public class BookedSeat {
	
    @Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
    private String seatId;
    private String screenId;
   
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.SSS")
    private LocalDateTime lockedAt;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    private Booking booking;  // ManyToOne - Each seat belongs to one Booking

    public BookedSeat(String seatId, String screenId, BookingStatus status, LocalDateTime lockedAt, Booking booking) {
        this.seatId = seatId;
        this.screenId = screenId;
        this.status = status;
        this.lockedAt = lockedAt;
        this.booking = booking;
    }

}
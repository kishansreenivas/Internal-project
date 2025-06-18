package com.MovieService.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter

@Entity
@Table(name = "showtimes")
public class Showtime {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;  // ✅ ManyToOne - Each showtime is for one movie

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;  // ✅ ManyToOne - Each showtime takes place in one theatre

    @ManyToOne
    @JoinColumn(name = "screen_id")
    private Screen screen;  // ✅ ManyToOne - Each showtime takes place in one screen


    private LocalDateTime showStart;
    private LocalDateTime showEnd;
    private String language;
  
}
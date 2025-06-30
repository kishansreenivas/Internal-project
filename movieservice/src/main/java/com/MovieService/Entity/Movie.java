package com.MovieService.Entity;

import java.time.LocalDate;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.MovieService.EnumType.MovieStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "movies")
public class Movie {
	  @Id
	    @GeneratedValue(generator = "UUID")
	    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	    private String id;
    private String title;
    private String language;
    private LocalDate releaseDate;
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    private MovieStatus status;

   
    // Getters and Setters
}
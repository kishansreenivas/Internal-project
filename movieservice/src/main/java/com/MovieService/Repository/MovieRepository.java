package com.MovieService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MovieService.Entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {




}

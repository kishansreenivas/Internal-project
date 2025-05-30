package com.MovieService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MovieService.Entity.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {}

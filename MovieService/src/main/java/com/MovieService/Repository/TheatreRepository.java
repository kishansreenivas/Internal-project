package com.MovieService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MovieService.Entity.Theatre;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {}

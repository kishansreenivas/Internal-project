package com.MovieService.Services;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MovieService.Entity.Genre;
import com.MovieService.Repository.GenreRepository;

import Exception.ResourceNotFoundException;

@Service
public class GenreService {
		@Autowired
    private final MovieService movieService;
	@Autowired
    private final GenreRepository genreRepository;
    public GenreService(GenreRepository genreRepository, MovieService movieService) {
        this.genreRepository = genreRepository;
        this.movieService = movieService;
    }

    public Genre createGenre(Genre genre) {
    	try {
    		 return genreRepository.save(genre);
		} catch (Exception e) {
			throw new ServiceException("Error creating Genre", e);
		}
       
    }

    public List<Genre> getAllGenres() {
    	try {
    		 return genreRepository.findAll();
		} catch (Exception e) {
			throw new ServiceException("Error retriving  getAllGenres");
		}
       
    }

    public Genre getGenreById(Long id) {
    	
    		return genreRepository.findById(id)
    				.orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + id)) ;
		    }

    public void deleteGenre(Long id) {
    
    if (!genreRepository.existsById(id)) {
        throw new ResourceNotFoundException("Movie not found with ID: " + id);
    }
    genreRepository.deleteById(id);
    }
}
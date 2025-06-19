package com.MovieService.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MovieService.Entity.Movie;
import com.MovieService.dto.ApiResponse;
import com.MovieService.service.impl.MovieServiceImpl;

@RestController
@RequestMapping("/v1/movies")
public class MovieController {

    @Autowired
    private MovieServiceImpl movieService;

    @PostMapping
    public ResponseEntity<ApiResponse<Movie>> create(@RequestBody Movie movie) {
        Movie created = movieService.createMovie(movie);
        return ResponseEntity.ok(new ApiResponse<>(true, created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Movie>>> getAll() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(new ApiResponse<>(true, movies));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable String id) {
        Movie movie = movieService.getMovieById(id);
        if (movie == null) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", "Movie not found")));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, movie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable String id, @RequestBody Movie movie) {
        Movie updated = movieService.updateMovie(id, movie);
        if (updated == null) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", "Movie not found or could not be updated")));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> delete(@PathVariable String id) {
        try {
            movieService.deleteMovie(id);
            return ResponseEntity.ok(new ApiResponse<>(true, Map.of("message", "Movie deleted successfully")));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", "Movie not found or already deleted")));
        }
    }
}

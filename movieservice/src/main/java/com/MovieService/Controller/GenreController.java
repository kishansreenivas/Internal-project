package com.MovieService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MovieService.Entity.Genre;
import com.MovieService.dto.ApiResponse;
import com.MovieService.service.impl.GenreServiceImpl;

@RestController
@RequestMapping("/v1/genres")
public class GenreController {

    @Autowired
    private GenreServiceImpl genreService;

    public GenreController(GenreServiceImpl genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Genre>> create(@RequestBody Genre genre) {
        Genre createdGenre = genreService.createGenre(genre);
        return ResponseEntity.ok(new ApiResponse<>(true, createdGenre));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Genre>>> getAll() {
        List<Genre> genres = genreService.getAllGenres();
        return ResponseEntity.ok(new ApiResponse<>(true, genres));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Genre>> getById(@PathVariable String id) {
        Genre genre = genreService.getGenreById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, genre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        genreService.deleteGenre(id);
        return ResponseEntity.ok(new ApiResponse<>(true, null));
    }
}

package com.MovieService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MovieService.Entity.Genre;
import com.MovieService.Services.GenreService;


@RestController
@RequestMapping("/genres")
public class GenreController {
	@Autowired
    private  GenreService genreService;
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    public ResponseEntity<Genre> create(@RequestBody Genre genre) {
        return ResponseEntity.ok(genreService.createGenre(genre));
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAll() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Genre> getById(@PathVariable String id) {
        Genre genre = genreService.getGenreById(id); // Will throw if not found
        return ResponseEntity.ok(genre);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}

package com.MovieService.Services;


import com.MovieService.Entity.Genre;

import java.util.List;

public interface GenreService {
    Genre createGenre(Genre genre);
    List<Genre> getAllGenres();
    Genre getGenreById(String id);
    void deleteGenre(String id);
}

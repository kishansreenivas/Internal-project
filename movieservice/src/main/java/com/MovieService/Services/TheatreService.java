package com.MovieService.Services;

import java.util.List;
import com.MovieService.Entity.Theatre;

public interface TheatreService {

    Theatre createTheatre(Theatre theatre);

    List<Theatre> getAllTheatres();

    Theatre getTheatreById(String id);

    Theatre updateTheatre(String id, Theatre updated);

    void deleteTheatre(String id);
}

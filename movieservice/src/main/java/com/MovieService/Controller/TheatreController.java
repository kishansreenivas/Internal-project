package com.MovieService.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.MovieService.Entity.Theatre;
import com.MovieService.Services.TheatreService;
import com.MovieService.dto.ApiResponse;
import com.MovieService.service.impl.TheatreServiceImpl;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/theatres")
public class TheatreController {

    @Autowired
    private TheatreServiceImpl theatreService;

    public TheatreController(TheatreServiceImpl theatreService) {
        this.theatreService = theatreService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody Theatre theatre) {
        try {
            Theatre created = theatreService.createTheatre(theatre);
            return ResponseEntity.ok(new ApiResponse<>(true, created));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", e.getMessage())));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Theatre>>> getAll() {
        List<Theatre> theatres = theatreService.getAllTheatres();
        return ResponseEntity.ok(new ApiResponse<>(true, theatres));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable String id) {
        try {
            Theatre theatre = theatreService.getTheatreById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, theatre));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", "Theatre not found")));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable String id, @RequestBody Theatre theatre) {
        try {
            Theatre updated = theatreService.updateTheatre(id, theatre);
            return ResponseEntity.ok(new ApiResponse<>(true, updated));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", e.getMessage())));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> delete(@PathVariable String id) {
        try {
            theatreService.deleteTheatre(id);
            return ResponseEntity.ok(new ApiResponse<>(true, Map.of("message", "Theatre deleted successfully")));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", e.getMessage())));
        }
    }
}

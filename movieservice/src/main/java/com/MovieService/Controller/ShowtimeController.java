package com.MovieService.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MovieService.Entity.Showtime;
import com.MovieService.dto.ApiResponse;
import com.MovieService.dto.ShowtimeRequestDto;
import com.MovieService.service.impl.ShowtimeServiceImpl;

@RestController
@RequestMapping("/v1/showtimes")
public class ShowtimeController {

    @Autowired
    private ShowtimeServiceImpl showtimeService;

    public ShowtimeController(ShowtimeServiceImpl showtimeService) {
        this.showtimeService = showtimeService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody ShowtimeRequestDto dto) {
        try {
            Showtime created = showtimeService.createShowtime(dto);
            return ResponseEntity.ok(new ApiResponse<>(true, created));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", e.getMessage())));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Showtime>>> getAll() {
        List<Showtime> showtimes = showtimeService.getAllShowtimes();
        return ResponseEntity.ok(new ApiResponse<>(true, showtimes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable String id) {
        Showtime showtime = showtimeService.getShowtimeById(id);
        if (showtime != null) {
            return ResponseEntity.ok(new ApiResponse<>(true, showtime));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", "Showtime not found")));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable String id, @RequestBody Showtime showtime) {
        try {
            Showtime updated = showtimeService.updateShowtime(id, showtime);
            return ResponseEntity.ok(new ApiResponse<>(true, updated));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", e.getMessage())));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> delete(@PathVariable String id) {
        try {
            showtimeService.deleteShowtime(id);
            return ResponseEntity.ok(new ApiResponse<>(true, Map.of("message", "Showtime deleted successfully")));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", e.getMessage())));
        }
    }
}

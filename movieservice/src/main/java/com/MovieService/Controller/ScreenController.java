package com.MovieService.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MovieService.Entity.Screen;
import com.MovieService.Entity.Theatre;
import com.MovieService.Repository.ScreenRepository;
import com.MovieService.Repository.TheatreRepository;
import com.MovieService.dto.ApiResponse;
import com.MovieService.dto.ScreenRequestDto;
import com.MovieService.service.impl.ScreenServiceImpl;

@RestController
@RequestMapping("/v1/screens")
public class ScreenController {

    @Autowired
    private ScreenServiceImpl screenService;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    public ScreenController(ScreenServiceImpl screenService) {
        this.screenService = screenService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Screen>> create(@RequestBody Screen screen) {
        return ResponseEntity.ok(new ApiResponse<>(true, screenService.createScreen(screen)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Screen>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(true, screenService.getAllScreens()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable String id) {
        Optional<Screen> screen = screenService.getScreenById(id);
        if (screen.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(true, screen.get()));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", "Screen not found")));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable String id, @RequestBody Screen screen) {
        Screen updated = screenService.updateScreen(id, screen);
        if (updated == null) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", "Update failed or screen not found")));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> delete(@PathVariable String id) {
        try {
            screenService.deleteScreen(id);
            return ResponseEntity.ok(new ApiResponse<>(true, Map.of("message", "Screen deleted successfully")));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", "Screen not found or could not be deleted")));
        }
    }

    @PostMapping("/with-theatre")
    public ResponseEntity<ApiResponse<?>> createScreenWithAddingTheatre(@RequestBody ScreenRequestDto request) {
        try {
            Theatre theatre = theatreRepository.findById(request.getTheatreId())
                    .orElseThrow(() -> new RuntimeException("Theatre not found"));

            Screen screen = new Screen();
            screen.setName(request.getName());
            screen.setTheatre(theatre);

            Screen savedScreen = screenRepository.save(screen);

            return ResponseEntity.ok(new ApiResponse<>(true, savedScreen));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", e.getMessage())));
        }
    }
}

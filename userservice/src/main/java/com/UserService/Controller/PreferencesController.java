package com.UserService.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.PreferencesDTO;
import com.UserService.Services.PreferencesService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/preferences")
@RequiredArgsConstructor
public class PreferencesController {

    private final PreferencesService preferencesService;

    @PostMapping
    public ResponseEntity<ApiResponse<PreferencesDTO>> create(@RequestBody PreferencesDTO dto) {
        PreferencesDTO saved = preferencesService.createPreferences(dto);
        return ResponseEntity.ok(ApiResponse.success(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PreferencesDTO>> update(@PathVariable UUID id, @RequestBody PreferencesDTO dto) {
        PreferencesDTO updated = preferencesService.updatePreferences(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PreferencesDTO>> getById(@PathVariable UUID id) {
        PreferencesDTO result = preferencesService.getPreferencesById(id);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PreferencesDTO>>> getAll() {
        List<PreferencesDTO> list = preferencesService.getAllPreferences();
        return ResponseEntity.ok(ApiResponse.success(list));
    }
}

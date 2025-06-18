package com.UserService.Services;

import java.util.List;
import java.util.UUID;

import com.UserService.Dto.PreferencesDTO;

public interface PreferencesService {
    PreferencesDTO createPreferences(PreferencesDTO dto);
    PreferencesDTO updatePreferences(UUID id, PreferencesDTO dto);
    PreferencesDTO getPreferencesById(UUID id);
    List<PreferencesDTO> getAllPreferences();
}

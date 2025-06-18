package com.UserService.Servicesimpl;

import com.UserService.Dto.PreferencesDTO;
import com.UserService.Entity.Preferences;
import com.UserService.Mapper.PreferencesMapper;
import com.UserService.Repositories.PreferencesRepository;
import com.UserService.Services.PreferencesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PreferencesServiceImpl implements PreferencesService {

    private final PreferencesRepository preferencesRepository;
    private final PreferencesMapper mapper;

    @Override
    public PreferencesDTO createPreferences(PreferencesDTO dto) {
        log.info("ENTRY: createPreferences() - DTO: {}", dto);

        validatePreferencesDTO(dto);

        Preferences preferences = mapper.toEntity(dto);
        Preferences saved = preferencesRepository.save(preferences);

        PreferencesDTO result = mapper.toDTO(saved);
        log.info("EXIT: createPreferences() - Saved: {}", result);
        return result;
    }

    @Override
    public PreferencesDTO updatePreferences(UUID id, PreferencesDTO dto) {
        log.info("ENTRY: updatePreferences() - ID: {}, DTO: {}", id, dto);

        validatePreferencesDTO(dto);

        Preferences existing = preferencesRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Preferences not found for ID: {}", id);
                    return new RuntimeException("Preferences not found");
                });

        existing.setNewsletterSubscribed(dto.isNewsletterSubscribed());
        existing.setLanguage(dto.getLanguage());
        existing.setRegion(dto.getRegion());

        Preferences updated = preferencesRepository.save(existing);
        PreferencesDTO result = mapper.toDTO(updated);

        log.info("EXIT: updatePreferences() - Updated: {}", result);
        return result;
    }

    @Override
    public PreferencesDTO getPreferencesById(UUID id) {
        log.info("ENTRY: getPreferencesById() - ID: {}", id);

        Preferences preferences = preferencesRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Preferences not found for ID: {}", id);
                    return new RuntimeException("Preferences not found");
                });

        PreferencesDTO result = mapper.toDTO(preferences);
        log.info("EXIT: getPreferencesById() - Result: {}", result);
        return result;
    }

    @Override
    public List<PreferencesDTO> getAllPreferences() {
        log.info("ENTRY: getAllPreferences()");

        List<PreferencesDTO> list = preferencesRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        log.info("EXIT: getAllPreferences() - Count: {}", list.size());
        return list;
    }

    private void validatePreferencesDTO(PreferencesDTO dto) {
        if (dto.getLanguage() == null || dto.getLanguage().trim().isEmpty()) {
            log.warn("Validation failed: Language is required");
            throw new IllegalArgumentException("Language is required");
        }
        if (dto.getRegion() == null || dto.getRegion().trim().isEmpty()) {
            log.warn("Validation failed: Region is required");
            throw new IllegalArgumentException("Region is required");
        }
        log.debug("Validation passed for PreferencesDTO: {}", dto);
    }
}

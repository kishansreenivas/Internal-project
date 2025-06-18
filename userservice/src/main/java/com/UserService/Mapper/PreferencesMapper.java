package com.UserService.Mapper;

import org.springframework.stereotype.Component;

import com.UserService.Dto.PreferencesDTO;
import com.UserService.Entity.Preferences;

@Component
public class PreferencesMapper {

    public PreferencesDTO toDTO(Preferences entity) {
        if (entity == null) return null;

        PreferencesDTO dto = new PreferencesDTO();
        dto.setId(entity.getId());
        dto.setNewsletterSubscribed(entity.isNewsletterSubscribed());
        dto.setLanguage(entity.getLanguage());
        dto.setRegion(entity.getRegion());
        return dto;
    }

    public Preferences toEntity(PreferencesDTO dto) {
        if (dto == null) return null;

        Preferences entity = new Preferences();
        entity.setId(dto.getId());
        entity.setNewsletterSubscribed(dto.isNewsletterSubscribed());
        entity.setLanguage(dto.getLanguage());
        entity.setRegion(dto.getRegion());
        return entity;
    }
}

package com.UserService.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.UserService.Entity.Preferences;

import java.util.UUID;

public interface PreferencesRepository extends JpaRepository<Preferences, UUID> {
}

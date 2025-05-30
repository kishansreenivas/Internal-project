package com.UserService.Repositories;



import org.springframework.data.jpa.repository.JpaRepository;

import com.UserService.Entity.Preferences;

public interface PreferencesRepository extends JpaRepository<Preferences, Long> {}

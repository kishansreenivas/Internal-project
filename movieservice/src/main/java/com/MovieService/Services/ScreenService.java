package com.MovieService.Services;

import com.MovieService.Entity.Screen;
import com.MovieService.dto.ScreenRequestDto;

import java.util.List;
import java.util.Optional;

public interface ScreenService {
    Screen createScreen(Screen screen);
    Screen createScreen(ScreenRequestDto dto);
    List<Screen> getAllScreens();
    Optional<Screen> getScreenById(String id);
    Screen updateScreen(String id, Screen updated);
    void deleteScreen(String id);
}

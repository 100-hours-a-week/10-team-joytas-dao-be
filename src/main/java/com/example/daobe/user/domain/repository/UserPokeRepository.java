package com.example.daobe.user.domain.repository;

import com.example.daobe.user.domain.event.UserPokeEvent;

public interface UserPokeRepository {

    void save(UserPokeEvent userPokeEvent);

    boolean existsByUserId(Long userId);
}

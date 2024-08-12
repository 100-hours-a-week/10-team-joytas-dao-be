package com.example.daobe.lounge.service;

import com.example.daobe.lounge.repository.LoungeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoungeService {

    private final LoungeRepository loungeRepository;
}

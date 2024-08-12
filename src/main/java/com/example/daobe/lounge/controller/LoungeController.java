package com.example.daobe.lounge.controller;

import com.example.daobe.lounge.service.LoungeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/lounges")
@RequiredArgsConstructor
public class LoungeController {

    private final LoungeService loungeService;

}

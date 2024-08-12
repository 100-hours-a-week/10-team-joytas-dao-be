package com.example.daobe.objet.controller;

import com.example.daobe.objet.service.ObjetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/objets")
@RequiredArgsConstructor
public class ObjetController {
    private final ObjetService objectService;
}

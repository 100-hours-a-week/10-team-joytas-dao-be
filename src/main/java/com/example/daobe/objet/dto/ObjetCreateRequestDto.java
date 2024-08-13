package com.example.daobe.objet.dto;

import java.util.List;

public record ObjetCreateRequestDto(List<Long> owners, String name, String description, String type, Long loungeId) {
}

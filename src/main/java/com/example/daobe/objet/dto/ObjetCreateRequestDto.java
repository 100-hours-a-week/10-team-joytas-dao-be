package com.example.daobe.objet.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjetCreateRequestDto {
    private List<Long> owners;
    private String name;
    private String description;
    private String type;
    private Long loungeId;
}

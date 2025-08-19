package com.example.postierhr.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CVDto {
    
    private UUID id;
    
    @Size(max = 2000, message = "Le résumé ne peut pas dépasser 2000 caractères")
    private String resume;
    
    @Size(max = 2000, message = "Les compétences ne peuvent pas dépasser 2000 caractères")
    private String competences;
    
    @Size(max = 2000, message = "La formation ne peut pas dépasser 2000 caractères")
    private String formation;
    
    @Size(max = 5000, message = "Les expériences ne peuvent pas dépasser 5000 caractères")
    private String experiences;
    
    @Size(max = 1000, message = "Les langues ne peuvent pas dépasser 1000 caractères")
    private String langues;
    
    private LocalDateTime derniereModification;
}
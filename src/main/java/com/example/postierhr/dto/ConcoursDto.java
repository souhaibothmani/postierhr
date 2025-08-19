package com.example.postierhr.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ConcoursDto {
    
    private UUID id;
    private String titre;
    private String gradeRequis;
    private String description;
    private LocalDate dateDebutCandidature;
    private LocalDate dateFinCandidature;
    private LocalDateTime dateCreation;
    private boolean ouvertAuxCandidatures;
    private boolean utilisateurDejaCandidat;
}
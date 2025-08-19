package com.example.postierhr.dto;

import com.example.postierhr.entity.DemandeConge;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DemandeCongeDto {
    
    private UUID id;
    
    @NotNull(message = "Le type de congé est obligatoire")
    private DemandeConge.TypeConge type;
    
    @NotNull(message = "La date de début est obligatoire")
    @FutureOrPresent(message = "La date de début ne peut pas être dans le passé")
    private LocalDate dateDebut;
    
    @NotNull(message = "La date de fin est obligatoire")
    @FutureOrPresent(message = "La date de fin ne peut pas être dans le passé")
    private LocalDate dateFin;
    
    @NotNull(message = "Le nombre de jours est obligatoire")
    @Positive(message = "Le nombre de jours doit être positif")
    private BigDecimal nombreJours;
    
    private DemandeConge.StatutDemande statut;
    private LocalDateTime dateCreation;
    
    private String utilisateurNom;
    private String utilisateurPrenom;
}
package com.example.postierhr.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SoldeCongesDto {
    
    private UUID id;
    private Integer annee;
    private BigDecimal joursAnnuels;
    private BigDecimal joursMaladie;
    private LocalDateTime dateModification;
}
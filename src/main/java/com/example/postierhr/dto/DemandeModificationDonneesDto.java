package com.example.postierhr.dto;

import com.example.postierhr.entity.DemandeModificationDonnees;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class DemandeModificationDonneesDto {
    
    private UUID id;
    
    @NotEmpty(message = "Le contenu de la demande ne peut pas être vide")
    private Map<String, Object> contenu;
    
    private DemandeModificationDonnees.StatutDemande statut;
    private LocalDateTime dateCreation;
    
    private String utilisateurNom;
    private String utilisateurPrenom;
}
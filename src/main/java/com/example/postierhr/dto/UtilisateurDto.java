package com.example.postierhr.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UtilisateurDto {
    
    private UUID id;
    private String matricule;
    private String email;
    private String prenom;
    private String nom;
    private String grade;
    private String bureau;
    private String departement;
    private Boolean actif;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }
}
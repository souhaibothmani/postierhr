package com.example.postierhr.dto;

import com.example.postierhr.entity.DemandeAttestation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DemandeAttestationDto {
    
    private UUID id;
    
    @NotNull(message = "Le type d'attestation est obligatoire")
    private DemandeAttestation.TypeAttestation type;
    
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    
    private LocalDateTime dateCreation;
    
    private String utilisateurNom;
    private String utilisateurPrenom;
    private String utilisateurMatricule;
    
    // Pour l'upload de fichiers
    private List<MultipartFile> documents;
    
    // Pour l'affichage des documents existants
    private List<DocumentDto> documentsExistants;
}
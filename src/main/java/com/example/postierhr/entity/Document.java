package com.example.postierhr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document")
@Data
@EqualsAndHashCode(callSuper = false)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nom_fichier", nullable = false)
    @NotBlank(message = "Le nom du fichier est obligatoire")
    private String nomFichier;

    @Column(name = "nom_original", nullable = false)
    @NotBlank(message = "Le nom original est obligatoire")
    private String nomOriginal;

    @Column(name = "type_mime", nullable = false)
    @NotBlank(message = "Le type MIME est obligatoire")
    private String typeMime;

    @Column(nullable = false)
    @NotNull(message = "La taille est obligatoire")
    private Long taille;

    @Column(name = "chemin_fichier", nullable = false)
    @NotBlank(message = "Le chemin du fichier est obligatoire")
    private String cheminFichier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_attestation_id")
    private DemandeAttestation demandeAttestation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_conge_id")
    private DemandeConge demandeConge;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDocument typeDocument = TypeDocument.JUSTIFICATIF;

    public enum TypeDocument {
        JUSTIFICATIF("Document justificatif"),
        PIECE_JOINTE("Pièce jointe"),
        FORMULAIRE("Formulaire");

        private final String libelle;

        TypeDocument(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    // Méthodes utilitaires
    public String getTailleFormatee() {
        if (taille == null) return "0 B";
        
        String[] units = {"B", "KB", "MB", "GB"};
        double size = taille;
        int unitIndex = 0;
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.1f %s", size, units[unitIndex]);
    }

    public boolean isImage() {
        return typeMime != null && typeMime.startsWith("image/");
    }

    public boolean isPdf() {
        return "application/pdf".equals(typeMime);
    }

    public boolean isWord() {
        return typeMime != null && (
            typeMime.equals("application/msword") ||
            typeMime.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        );
    }
}
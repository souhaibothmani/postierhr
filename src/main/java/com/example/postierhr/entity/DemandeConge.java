package com.example.postierhr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "demande_conge")
@Data
@EqualsAndHashCode(callSuper = false)
public class DemandeConge {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Le type de congé est obligatoire")
    private TypeConge type;

    @Column(name = "date_debut", nullable = false)
    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @Column(name = "nombre_jours", nullable = false, precision = 4, scale = 1)
    private BigDecimal nombreJours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDemande statut = StatutDemande.ENVOYE;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    public enum TypeConge {
        ANNUEL("Congé annuel"),
        MALADIE("Congé maladie"),
        SANS_SOLDE("Congé sans solde"),
        AUTRE("Autre");

        private final String libelle;

        TypeConge(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    public enum StatutDemande {
        ENVOYE("Envoyé"),
        EN_COURS("En cours"),
        APPROUVE("Approuvé"),
        REJETE("Rejeté");

        private final String libelle;

        StatutDemande(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }
}
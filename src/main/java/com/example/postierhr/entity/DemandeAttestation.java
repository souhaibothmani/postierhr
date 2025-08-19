package com.example.postierhr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "demande_attestation")
@Data
@EqualsAndHashCode(callSuper = false)
public class DemandeAttestation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Le type d'attestation est obligatoire")
    private TypeAttestation type;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(length = 500)
    private String motif;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy = "demandeAttestation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Document> documents;

    public enum TypeAttestation {
        TRAVAIL("Attestation de travail"),
        SALAIRE("Attestation de salaire");

        private final String libelle;

        TypeAttestation(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

}
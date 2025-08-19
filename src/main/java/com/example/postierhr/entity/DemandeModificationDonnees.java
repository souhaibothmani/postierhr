package com.example.postierhr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "demande_modification_donnees")
@Data
@EqualsAndHashCode(callSuper = false)
public class DemandeModificationDonnees {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contenu", nullable = false)
    private Map<String, Object> contenu;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDemande statut = StatutDemande.ENVOYE;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    public enum StatutDemande {
        ENVOYE, EN_COURS, APPROUVE, REJETE
    }
}
package com.example.postierhr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "donnees_personnelles")
@Data
@EqualsAndHashCode(callSuper = false)
public class DonneesPersonnelles {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "utilisateur_id", nullable = false)
    @NotNull(message = "L'utilisateur est obligatoire")
    private UUID utilisateurId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", insertable = false, updatable = false)
    private Utilisateur utilisateur;

    @Column(length = 500)
    private String adresse;

    @Column(length = 20)
    private String telephone;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(length = 100)
    private String departement;

    @Column(length = 100)
    private String bureau;

    @Column(name = "code_postal", length = 10)
    private String codePostal;

    @Column(length = 100)
    private String ville;

    @Column(length = 100)
    private String situationFamiliale;

    @Column(name = "nombre_enfants")
    private Integer nombreEnfants;

    @UpdateTimestamp
    @Column(name = "date_maj")
    private LocalDateTime dateMaj;
}
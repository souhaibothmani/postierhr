package com.example.postierhr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "utilisateur")
@Data
@EqualsAndHashCode(callSuper = false)
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;

    @Column(unique = true, nullable = false)
    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @Column(name = "mot_de_passe_hash", nullable = false)
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasseHash;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @Column(length = 50)
    private String grade;

    @Column(length = 100)
    private String bureau;

    @Column(length = 100)
    private String departement;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(nullable = false)
    private Boolean actif = true;

    public String getNomComplet() {
        return prenom + " " + nom;
    }
}
package com.example.postierhr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "concours")
@Data
@EqualsAndHashCode(callSuper = false)
public class Concours {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(name = "grade_requis", length = 50)
    private String gradeRequis;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_debut_candidature")
    private LocalDate dateDebutCandidature;

    @Column(name = "date_fin_candidature")
    private LocalDate dateFinCandidature;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    public boolean isOuvertAuxCandidatures() {
        LocalDate today = LocalDate.now();
        return dateDebutCandidature != null && dateFinCandidature != null &&
               !today.isBefore(dateDebutCandidature) && !today.isAfter(dateFinCandidature);
    }
}
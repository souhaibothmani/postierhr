package com.example.postierhr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cv")
@Data
@EqualsAndHashCode(callSuper = false)
public class CV {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    @Column(columnDefinition = "TEXT")
    private String resume;

    @Column(columnDefinition = "TEXT")
    private String competences;

    @Column(columnDefinition = "TEXT")
    private String formation;

    @Column(columnDefinition = "TEXT")
    private String experiences;

    @Column(columnDefinition = "TEXT")
    private String langues;

    @UpdateTimestamp
    @Column(name = "derniere_modification")
    private LocalDateTime derniereModification;
}
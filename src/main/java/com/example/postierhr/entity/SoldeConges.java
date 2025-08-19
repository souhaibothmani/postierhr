package com.example.postierhr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solde_conges", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"utilisateur_id", "annee"}))
@Data
@EqualsAndHashCode(callSuper = false)
public class SoldeConges {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Column(nullable = false)
    @NotNull(message = "L'année est obligatoire")
    private Integer annee;

    @Column(name = "jours_annuels", nullable = false, precision = 4, scale = 1)
    private BigDecimal joursAnnuels = BigDecimal.valueOf(25.0);

    @Column(name = "jours_maladie", nullable = false, precision = 4, scale = 1)
    private BigDecimal joursMaladie = BigDecimal.valueOf(30.0);

    @UpdateTimestamp
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

}
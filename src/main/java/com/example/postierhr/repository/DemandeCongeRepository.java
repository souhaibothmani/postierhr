package com.example.postierhr.repository;

import com.example.postierhr.entity.DemandeConge;
import com.example.postierhr.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DemandeCongeRepository extends JpaRepository<DemandeConge, UUID> {

    List<DemandeConge> findByUtilisateurOrderByDateCreationDesc(Utilisateur utilisateur);
    
    List<DemandeConge> findByUtilisateurIdOrderByDateCreationDesc(UUID utilisateurId);
    
    List<DemandeConge> findByStatutOrderByDateCreationDesc(DemandeConge.StatutDemande statut);
    
    List<DemandeConge> findByTypeAndUtilisateurOrderByDateCreationDesc(
        DemandeConge.TypeConge type, Utilisateur utilisateur);
    
    @Query("SELECT COUNT(d) > 0 FROM DemandeConge d WHERE d.utilisateur.id = :utilisateurId AND d.dateCreation >= :dateDebut")
    boolean existsByUtilisateurIdAndDateCreationAfter(@Param("utilisateurId") UUID utilisateurId, @Param("dateDebut") LocalDateTime dateDebut);
    
    @Query("SELECT d FROM DemandeConge d WHERE d.utilisateur.id = :utilisateurId " +
           "AND d.statut IN ('APPROUVE', 'ENVOYE') " +
           "AND ((d.dateDebut BETWEEN :dateDebut AND :dateFin) OR " +
           "(d.dateFin BETWEEN :dateDebut AND :dateFin) OR " +
           "(d.dateDebut <= :dateDebut AND d.dateFin >= :dateFin))")
    List<DemandeConge> findCongesEnConflit(@Param("utilisateurId") UUID utilisateurId, @Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
}
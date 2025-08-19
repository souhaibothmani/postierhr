package com.example.postierhr.repository;

import com.example.postierhr.entity.DemandeAttestation;
import com.example.postierhr.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DemandeAttestationRepository extends JpaRepository<DemandeAttestation, UUID> {

    List<DemandeAttestation> findByUtilisateurOrderByDateCreationDesc(Utilisateur utilisateur);
    
    List<DemandeAttestation> findByUtilisateurIdOrderByDateCreationDesc(UUID utilisateurId);
    
    List<DemandeAttestation> findAllByOrderByDateCreationDesc();
    
    List<DemandeAttestation> findByTypeAndUtilisateurOrderByDateCreationDesc(
        DemandeAttestation.TypeAttestation type, Utilisateur utilisateur);
    
    @Query("SELECT COUNT(d) > 0 FROM DemandeAttestation d WHERE d.utilisateur.id = :utilisateurId AND d.type = :type AND d.dateCreation >= :dateDebut")
    boolean existsByUtilisateurIdAndTypeAndDateCreationAfter(@Param("utilisateurId") UUID utilisateurId, @Param("type") DemandeAttestation.TypeAttestation type, @Param("dateDebut") LocalDateTime dateDebut);
}
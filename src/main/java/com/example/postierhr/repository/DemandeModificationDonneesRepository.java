package com.example.postierhr.repository;

import com.example.postierhr.entity.DemandeModificationDonnees;
import com.example.postierhr.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DemandeModificationDonneesRepository extends JpaRepository<DemandeModificationDonnees, UUID> {
    
    List<DemandeModificationDonnees> findByUtilisateurOrderByDateCreationDesc(Utilisateur utilisateur);
    
    List<DemandeModificationDonnees> findByUtilisateurIdOrderByDateCreationDesc(UUID utilisateurId);
    
    @Query("SELECT COUNT(d) > 0 FROM DemandeModificationDonnees d WHERE d.utilisateur.id = :utilisateurId AND d.dateCreation >= :dateDebut")
    boolean existsByUtilisateurIdAndDateCreationAfter(@Param("utilisateurId") UUID utilisateurId, @Param("dateDebut") LocalDateTime dateDebut);
}
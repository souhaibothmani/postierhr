package com.example.postierhr.repository;

import com.example.postierhr.entity.CandidatureConcours;
import com.example.postierhr.entity.Concours;
import com.example.postierhr.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidatureConcoursRepository extends JpaRepository<CandidatureConcours, UUID> {
    
    List<CandidatureConcours> findByUtilisateurOrderByDateCreationDesc(Utilisateur utilisateur);
    
    List<CandidatureConcours> findByUtilisateurIdOrderByDateCreationDesc(UUID utilisateurId);
    
    List<CandidatureConcours> findByConcoursOrderByDateCreationDesc(Concours concours);
    
    Optional<CandidatureConcours> findByUtilisateurAndConcours(Utilisateur utilisateur, Concours concours);
    
    boolean existsByUtilisateurAndConcours(Utilisateur utilisateur, Concours concours);
    
    boolean existsByUtilisateurIdAndConcoursId(UUID utilisateurId, UUID concoursId);
    
    @Query("SELECT cc FROM CandidatureConcours cc JOIN cc.concours c WHERE cc.utilisateur.id = :utilisateurId AND c.dateFinCandidature >= CURRENT_DATE ORDER BY cc.dateCreation DESC")
    List<CandidatureConcours> findCandidaturesActivesParUtilisateur(@Param("utilisateurId") UUID utilisateurId);
}
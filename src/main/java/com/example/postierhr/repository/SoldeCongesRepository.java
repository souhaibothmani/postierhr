package com.example.postierhr.repository;

import com.example.postierhr.entity.SoldeConges;
import com.example.postierhr.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SoldeCongesRepository extends JpaRepository<SoldeConges, UUID> {

    Optional<SoldeConges> findByUtilisateurAndAnnee(Utilisateur utilisateur, Integer annee);
    
    Optional<SoldeConges> findByUtilisateurIdAndAnnee(UUID utilisateurId, Integer annee);
    
    List<SoldeConges> findByUtilisateurOrderByAnneeDesc(Utilisateur utilisateur);
    
    List<SoldeConges> findByUtilisateurIdOrderByAnneeDesc(UUID utilisateurId);
    
    List<SoldeConges> findByAnnee(Integer annee);
    
    boolean existsByUtilisateurAndAnnee(Utilisateur utilisateur, Integer annee);
}
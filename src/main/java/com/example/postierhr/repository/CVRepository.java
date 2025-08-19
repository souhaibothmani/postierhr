package com.example.postierhr.repository;

import com.example.postierhr.entity.CV;
import com.example.postierhr.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CVRepository extends JpaRepository<CV, UUID> {
    
    Optional<CV> findByUtilisateur(Utilisateur utilisateur);
    
    Optional<CV> findByUtilisateurId(UUID utilisateurId);
    
    boolean existsByUtilisateur(Utilisateur utilisateur);
    
    boolean existsByUtilisateurId(UUID utilisateurId);
}
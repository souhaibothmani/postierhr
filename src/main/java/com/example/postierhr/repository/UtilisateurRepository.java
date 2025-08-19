package com.example.postierhr.repository;

import com.example.postierhr.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {

    Optional<Utilisateur> findByEmail(String email);
    
    Optional<Utilisateur> findByMatricule(String matricule);
    
    boolean existsByEmail(String email);
    
    boolean existsByMatricule(String matricule);
    
    List<Utilisateur> findByActifTrueOrderByNomAscPrenomAsc();
    
    List<Utilisateur> findByGradeAndActifTrueOrderByNomAscPrenomAsc(String grade);
    
    @Query("SELECT u FROM Utilisateur u WHERE u.actif = true AND " +
           "(LOWER(u.nom) LIKE LOWER(CONCAT('%', :recherche, '%')) OR " +
           "LOWER(u.prenom) LIKE LOWER(CONCAT('%', :recherche, '%')) OR " +
           "LOWER(u.matricule) LIKE LOWER(CONCAT('%', :recherche, '%')))")
    List<Utilisateur> rechercherUtilisateurs(@Param("recherche") String recherche);
}
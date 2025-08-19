package com.example.postierhr.repository;

import com.example.postierhr.entity.DonneesPersonnelles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DonneesPersonnellesRepository extends JpaRepository<DonneesPersonnelles, UUID> {

    Optional<DonneesPersonnelles> findByUtilisateurId(UUID utilisateurId);
    
    void deleteByUtilisateurId(UUID utilisateurId);
}
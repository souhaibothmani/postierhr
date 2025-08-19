package com.example.postierhr.repository;

import com.example.postierhr.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    
    List<Document> findByDemandeAttestationIdOrderByDateCreationDesc(UUID demandeAttestationId);
    
    List<Document> findByDemandeCongeIdOrderByDateCreationDesc(UUID demandeCongeId);
    
    
    List<Document> findByUtilisateurIdOrderByDateCreationDesc(UUID utilisateurId);
    
    @Query("SELECT d FROM Document d WHERE d.utilisateur.id = :utilisateurId AND d.typeDocument = :typeDocument ORDER BY d.dateCreation DESC")
    List<Document> findByUtilisateurIdAndTypeDocumentOrderByDateCreationDesc(
        @Param("utilisateurId") UUID utilisateurId, 
        @Param("typeDocument") Document.TypeDocument typeDocument
    );
    
    @Query("SELECT COUNT(d) FROM Document d WHERE d.utilisateur.id = :utilisateurId")
    Long countByUtilisateurId(@Param("utilisateurId") UUID utilisateurId);
    
    @Query("SELECT COALESCE(SUM(d.taille), 0) FROM Document d WHERE d.utilisateur.id = :utilisateurId")
    Long getTotalSizeByUtilisateurId(@Param("utilisateurId") UUID utilisateurId);
}
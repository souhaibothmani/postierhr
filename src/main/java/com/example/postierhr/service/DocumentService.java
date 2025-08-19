package com.example.postierhr.service;

import com.example.postierhr.entity.Document;
import com.example.postierhr.entity.DemandeAttestation;
import com.example.postierhr.entity.Utilisateur;
import com.example.postierhr.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DocumentService {

    private final DocumentRepository documentRepository;
    
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    
    // Types de fichiers autorisés
    private static final List<String> ALLOWED_MIME_TYPES = List.of(
        "application/pdf",
        "image/jpeg",
        "image/jpg", 
        "image/png",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    
    // Taille maximale : 5MB
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    
    public List<Document> saveDocuments(List<MultipartFile> files, DemandeAttestation demandeAttestation, Utilisateur utilisateur) {
        List<Document> documents = new ArrayList<>();
        
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            
            validateFile(file);
            
            try {
                Document document = saveDocument(file, utilisateur);
                document.setDemandeAttestation(demandeAttestation);
                document.setTypeDocument(Document.TypeDocument.JUSTIFICATIF);
                documents.add(documentRepository.save(document));
                
                log.info("Document sauvegardé: {} pour l'utilisateur {}", 
                    file.getOriginalFilename(), utilisateur.getMatricule());
                    
            } catch (IOException e) {
                log.error("Erreur lors de la sauvegarde du fichier: {}", file.getOriginalFilename(), e);
                throw new RuntimeException("Erreur lors de la sauvegarde du fichier: " + file.getOriginalFilename(), e);
            }
        }
        
        return documents;
    }
    
    private Document saveDocument(MultipartFile file, Utilisateur utilisateur) throws IOException {
        // Créer le répertoire de destination s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Générer un nom de fichier unique
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        Path filePath = uploadPath.resolve(uniqueFileName);
        
        // Sauvegarder le fichier
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Créer l'entité Document
        Document document = new Document();
        document.setNomFichier(uniqueFileName);
        document.setNomOriginal(originalFileName);
        document.setTypeMime(file.getContentType());
        document.setTaille(file.getSize());
        document.setCheminFichier(filePath.toString());
        document.setUtilisateur(utilisateur);
        
        return document;
    }
    
    private void validateFile(MultipartFile file) {
        // Vérifier la taille
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                "Le fichier " + file.getOriginalFilename() + " est trop volumineux. Taille maximale autorisée : 5MB");
        }
        
        // Vérifier le type MIME
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(
                "Type de fichier non autorisé pour " + file.getOriginalFilename() + ". Types acceptés : PDF, JPG, PNG, DOC, DOCX");
        }
        
        // Vérifier l'extension
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nom de fichier invalide");
        }
    }
    
    public List<Document> findByDemandeAttestation(UUID demandeAttestationId) {
        return documentRepository.findByDemandeAttestationIdOrderByDateCreationDesc(demandeAttestationId);
    }
    
    public Document findById(UUID id) {
        return documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document non trouvé"));
    }
    
    public void deleteDocument(UUID id) {
        Document document = findById(id);
        
        try {
            // Supprimer le fichier physique
            Path filePath = Paths.get(document.getCheminFichier());
            Files.deleteIfExists(filePath);
            
            // Supprimer l'entité
            documentRepository.delete(document);
            
            log.info("Document supprimé: {}", document.getNomOriginal());
            
        } catch (IOException e) {
            log.error("Erreur lors de la suppression du fichier: {}", document.getCheminFichier(), e);
            throw new RuntimeException("Erreur lors de la suppression du fichier", e);
        }
    }
    
    public byte[] getFileContent(UUID documentId) throws IOException {
        Document document = findById(documentId);
        Path filePath = Paths.get(document.getCheminFichier());
        
        if (!Files.exists(filePath)) {
            throw new RuntimeException("Fichier non trouvé sur le disque: " + document.getNomOriginal());
        }
        
        return Files.readAllBytes(filePath);
    }
}
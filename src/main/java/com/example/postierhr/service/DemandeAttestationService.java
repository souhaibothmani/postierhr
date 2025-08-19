package com.example.postierhr.service;

import com.example.postierhr.dto.DemandeAttestationDto;
import com.example.postierhr.entity.DemandeAttestation;
import com.example.postierhr.entity.Document;
import com.example.postierhr.entity.Utilisateur;
import com.example.postierhr.repository.DemandeAttestationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DemandeAttestationService {

    private final DemandeAttestationRepository demandeAttestationRepository;
    private final UtilisateurService utilisateurService;
    private final DocumentService documentService;

    @Transactional(readOnly = true)
    public List<DemandeAttestation> listerDemandesParUtilisateur(Utilisateur utilisateur) {
        return demandeAttestationRepository.findByUtilisateurOrderByDateCreationDesc(utilisateur);
    }

    @Transactional(readOnly = true)
    public List<DemandeAttestation> listerDemandesUtilisateurConnecte() {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        return listerDemandesParUtilisateur(utilisateur);
    }

    @Transactional(readOnly = true)
    public List<DemandeAttestation> listerToutesLesDemandes() {
        return demandeAttestationRepository.findAllByOrderByDateCreationDesc();
    }

    public DemandeAttestation creerDemande(DemandeAttestationDto demandeDto) {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        
        // Vérifier la période de refroidissement (7 jours) pour le même type
        LocalDateTime dateRefroidissement = LocalDateTime.now().minusDays(7);
        if (demandeAttestationRepository.existsByUtilisateurIdAndTypeAndDateCreationAfter(
                utilisateur.getId(), demandeDto.getType(), dateRefroidissement)) {
            throw new IllegalArgumentException("Vous avez déjà soumis une demande d'attestation de ce type dans les 7 derniers jours");
        }
        
        DemandeAttestation demande = new DemandeAttestation();
        demande.setUtilisateur(utilisateur);
        demande.setType(demandeDto.getType());
        demande.setDateDebut(demandeDto.getDateDebut());
        demande.setDateFin(demandeDto.getDateFin());
        demande.setMotif(demandeDto.getMotif());
        
        // Sauvegarder la demande d'abord
        demande = demandeAttestationRepository.save(demande);
        
        // Traiter les documents uploadés si présents
        if (demandeDto.getDocuments() != null && !demandeDto.getDocuments().isEmpty()) {
            List<MultipartFile> fichiers = demandeDto.getDocuments();
            List<Document> documents = documentService.saveDocuments(fichiers, demande, utilisateur);
            demande.setDocuments(documents);
        }
        
        return demande;
    }

    public void supprimerDemande(UUID demandeId) {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        
        DemandeAttestation demande = demandeAttestationRepository.findById(demandeId)
            .orElseThrow(() -> new IllegalArgumentException("Demande d'attestation non trouvée"));
        
        // Vérifier que la demande appartient à l'utilisateur connecté
        if (!demande.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à supprimer cette demande");
        }
        
        // Supprimer les documents associés s'il y en a
        if (demande.getDocuments() != null && !demande.getDocuments().isEmpty()) {
            for (Document document : demande.getDocuments()) {
                documentService.deleteDocument(document.getId());
            }
        }
        
        // Supprimer la demande
        demandeAttestationRepository.delete(demande);
    }
}
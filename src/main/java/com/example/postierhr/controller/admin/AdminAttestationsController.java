package com.example.postierhr.controller.admin;

import com.example.postierhr.dto.DemandeAttestationDto;
import com.example.postierhr.dto.DocumentDto;
import com.example.postierhr.entity.DemandeAttestation;
import com.example.postierhr.entity.Document;
import com.example.postierhr.service.DemandeAttestationService;
import com.example.postierhr.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminAttestationsController {

    private final DemandeAttestationService demandeAttestationService;
    private final DocumentService documentService;

    @GetMapping("/attestations")
    public String pageAttestationsAdmin(Model model) {
        try {
            List<DemandeAttestation> toutes = demandeAttestationService.listerToutesLesDemandes();
            
            List<DemandeAttestationDto> attestationsDto = toutes.stream().map(demande -> {
                DemandeAttestationDto dto = new DemandeAttestationDto();
                dto.setId(demande.getId());
                dto.setType(demande.getType());
                dto.setDateDebut(demande.getDateDebut());
                dto.setDateFin(demande.getDateFin());
                dto.setMotif(demande.getMotif());
                dto.setDateCreation(demande.getDateCreation());
                dto.setUtilisateurNom(demande.getUtilisateur().getNom());
                dto.setUtilisateurPrenom(demande.getUtilisateur().getPrenom());
                dto.setUtilisateurMatricule(demande.getUtilisateur().getMatricule());
                
                // Ajouter les documents avec leurs détails
                if (demande.getDocuments() != null && !demande.getDocuments().isEmpty()) {
                    List<DocumentDto> documentsDto = demande.getDocuments().stream().map(doc -> {
                        DocumentDto docDto = new DocumentDto();
                        docDto.setId(doc.getId());
                        docDto.setNomFichier(doc.getNomFichier());
                        docDto.setNomOriginal(doc.getNomOriginal());
                        docDto.setTypeMime(doc.getTypeMime());
                        docDto.setTaille(doc.getTaille());
                        docDto.setDateCreation(doc.getDateCreation());
                        docDto.setTypeDocument(doc.getTypeDocument());
                        docDto.setUtilisateurNom(doc.getUtilisateur().getNom());
                        docDto.setUtilisateurPrenom(doc.getUtilisateur().getPrenom());
                        return docDto;
                    }).collect(Collectors.toList());
                    dto.setDocumentsExistants(documentsDto);
                }
                
                return dto;
            }).collect(Collectors.toList());
            
            model.addAttribute("attestations", attestationsDto);
            return "admin/attestations";
            
        } catch (Exception e) {
            log.error("Erreur lors du chargement des attestations admin", e);
            model.addAttribute("error", "Erreur lors du chargement des données");
            return "admin/attestations";
        }
    }

    @GetMapping("/api/attestations")
    @ResponseBody
    public ResponseEntity<List<DemandeAttestationDto>> obtenirToutesLesAttestations() {
        try {
            List<DemandeAttestation> demandes = demandeAttestationService.listerToutesLesDemandes();
            
            List<DemandeAttestationDto> dtos = demandes.stream().map(demande -> {
                DemandeAttestationDto dto = new DemandeAttestationDto();
                dto.setId(demande.getId());
                dto.setType(demande.getType());
                dto.setDateDebut(demande.getDateDebut());
                dto.setDateFin(demande.getDateFin());
                dto.setMotif(demande.getMotif());
                dto.setDateCreation(demande.getDateCreation());
                dto.setUtilisateurNom(demande.getUtilisateur().getNom());
                dto.setUtilisateurPrenom(demande.getUtilisateur().getPrenom());
                dto.setUtilisateurMatricule(demande.getUtilisateur().getMatricule());
                
                // Documents avec informations complètes
                if (demande.getDocuments() != null && !demande.getDocuments().isEmpty()) {
                    List<DocumentDto> documentsDto = demande.getDocuments().stream().map(doc -> {
                        DocumentDto docDto = new DocumentDto();
                        docDto.setId(doc.getId());
                        docDto.setNomFichier(doc.getNomFichier());
                        docDto.setNomOriginal(doc.getNomOriginal());
                        docDto.setTypeMime(doc.getTypeMime());
                        docDto.setTaille(doc.getTaille());
                        docDto.setDateCreation(doc.getDateCreation());
                        docDto.setTypeDocument(doc.getTypeDocument());
                        docDto.setUtilisateurNom(doc.getUtilisateur().getNom());
                        docDto.setUtilisateurPrenom(doc.getUtilisateur().getPrenom());
                        return docDto;
                    }).collect(Collectors.toList());
                    dto.setDocumentsExistants(documentsDto);
                }
                
                return dto;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(dtos);
            
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des attestations admin", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/documents/{documentId}/approve")
    @ResponseBody
    public ResponseEntity<?> approuverDocument(@PathVariable UUID documentId) {
        try {
            log.info("Document {} approuvé par l'administrateur", documentId);
            return ResponseEntity.ok().body("{\"message\": \"Document approuvé avec succès\"}");
        } catch (Exception e) {
            log.error("Erreur lors de l'approbation du document {}", documentId, e);
            return ResponseEntity.badRequest().body("{\"error\": \"Erreur lors de l'approbation\"}");
        }
    }

    @PostMapping("/api/documents/{documentId}/reject")
    @ResponseBody
    public ResponseEntity<?> rejeterDocument(@PathVariable UUID documentId, @RequestParam String raison) {
        try {
            log.info("Document {} rejeté par l'administrateur. Raison: {}", documentId, raison);
            return ResponseEntity.ok().body("{\"message\": \"Document rejeté\"}");
        } catch (Exception e) {
            log.error("Erreur lors du rejet du document {}", documentId, e);
            return ResponseEntity.badRequest().body("{\"error\": \"Erreur lors du rejet\"}");
        }
    }
}
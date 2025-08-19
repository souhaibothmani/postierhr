package com.example.postierhr.controller;

import com.example.postierhr.dto.DemandeAttestationDto;
import com.example.postierhr.dto.DocumentDto;
import com.example.postierhr.entity.DemandeAttestation;
import com.example.postierhr.entity.Document;
import com.example.postierhr.service.DemandeAttestationService;
import com.example.postierhr.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DemandeAttestationController {

    private final DemandeAttestationService demandeAttestationService;
    private final DocumentService documentService;

    @GetMapping("/attestations")
    public String pageAttestations(Model model) {
        List<DemandeAttestation> demandes = demandeAttestationService.listerDemandesUtilisateurConnecte();
        model.addAttribute("demandes", demandes);
        model.addAttribute("nouvelleDemande", new DemandeAttestationDto());
        return "postier/attestations";
    }

    @GetMapping("/api/attestations")
    @ResponseBody
    public ResponseEntity<List<DemandeAttestationDto>> obtenirMesAttestations() {
        try {
            List<DemandeAttestation> demandes = demandeAttestationService.listerDemandesUtilisateurConnecte();
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
                
                // Ajouter les documents existants
                List<DocumentDto> documentsDto = demande.getDocuments().stream().map(doc -> {
                    DocumentDto docDto = new DocumentDto();
                    docDto.setId(doc.getId());
                    docDto.setNomFichier(doc.getNomFichier());
                    docDto.setNomOriginal(doc.getNomOriginal());
                    docDto.setTypeMime(doc.getTypeMime());
                    docDto.setTaille(doc.getTaille());
                    docDto.setDateCreation(doc.getDateCreation());
                    docDto.setTypeDocument(doc.getTypeDocument());
                    return docDto;
                }).collect(Collectors.toList());
                dto.setDocumentsExistants(documentsDto);
                
                return dto;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des attestations", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/attestations")
    public String creerDemandeAttestation(
            @ModelAttribute @Valid DemandeAttestationDto demandeDto,
            @RequestParam(value = "documents", required = false) List<MultipartFile> files,
            RedirectAttributes redirectAttributes) {
        try {
            // Ajouter les fichiers au DTO
            if (files != null && !files.isEmpty()) {
                demandeDto.setDocuments(files);
            }
            
            DemandeAttestation demande = demandeAttestationService.creerDemande(demandeDto);
            redirectAttributes.addFlashAttribute("successMessage", "Demande d'attestation créée avec succès");
            return "redirect:/attestations";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/attestations";
        } catch (Exception e) {
            log.error("Erreur lors de la création de la demande d'attestation", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la création de la demande");
            return "redirect:/attestations";
        }
    }
    
    @GetMapping("/api/documents/{documentId}/download")
    public ResponseEntity<byte[]> telechargerDocument(@PathVariable UUID documentId) {
        try {
            Document document = documentService.findById(documentId);
            byte[] fileContent = documentService.getFileContent(documentId);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getNomOriginal() + "\"")
                    .contentType(MediaType.parseMediaType(document.getTypeMime()))
                    .body(fileContent);
                    
        } catch (IOException e) {
            log.error("Erreur lors du téléchargement du document {}", documentId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erreur lors du téléchargement du document {}", documentId, e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/api/documents/{documentId}")
    @ResponseBody
    public ResponseEntity<?> supprimerDocument(@PathVariable UUID documentId) {
        try {
            documentService.deleteDocument(documentId);
            return ResponseEntity.ok().body("{\"message\": \"Document supprimé avec succès\"}");
        } catch (Exception e) {
            log.error("Erreur lors de la suppression du document {}", documentId, e);
            return ResponseEntity.badRequest().body("{\"error\": \"Une erreur s'est produite lors de la suppression\"}");
        }
    }

    @DeleteMapping("/api/attestations/{demandeId}")
    @ResponseBody
    public ResponseEntity<?> supprimerDemandeAttestation(@PathVariable UUID demandeId) {
        try {
            demandeAttestationService.supprimerDemande(demandeId);
            return ResponseEntity.ok().body("{\"message\": \"Demande d'attestation annulée avec succès\"}");
        } catch (IllegalArgumentException e) {
            log.error("Erreur lors de la suppression de la demande d'attestation {}: {}", demandeId, e.getMessage());
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de la demande d'attestation {}", demandeId, e);
            return ResponseEntity.badRequest().body("{\"error\": \"Une erreur s'est produite lors de l'annulation\"}");
        }
    }
}
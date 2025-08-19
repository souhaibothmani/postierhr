package com.example.postierhr.controller;

import com.example.postierhr.dto.DemandeModificationDonneesDto;
import com.example.postierhr.entity.DemandeModificationDonnees;
import com.example.postierhr.entity.Utilisateur;
import com.example.postierhr.repository.DemandeModificationDonneesRepository;
import com.example.postierhr.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
@Slf4j
public class PersonalDataController {

    private final UtilisateurService utilisateurService;
    private final DemandeModificationDonneesRepository demandeModificationDonneesRepository;

    @GetMapping("/change-requests")
    public ResponseEntity<List<DemandeModificationDonneesDto>> obtenirMesDemandesModification() {
        try {
            Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
            List<DemandeModificationDonnees> demandes = demandeModificationDonneesRepository
                .findByUtilisateurOrderByDateCreationDesc(utilisateur);
            
            List<DemandeModificationDonneesDto> dtos = demandes.stream().map(demande -> {
                DemandeModificationDonneesDto dto = new DemandeModificationDonneesDto();
                dto.setId(demande.getId());
                dto.setContenu(demande.getContenu());
                dto.setStatut(demande.getStatut());
                dto.setDateCreation(demande.getDateCreation());
                dto.setUtilisateurNom(demande.getUtilisateur().getNom());
                dto.setUtilisateurPrenom(demande.getUtilisateur().getPrenom());
                return dto;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des demandes de modification", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/change-requests")
    public ResponseEntity<?> creerDemandeModification(@Valid @RequestBody DemandeModificationDonneesDto demandeDto) {
        try {
            Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
            
            // Vérifier la période de refroidissement (7 jours)
            LocalDateTime dateRefroidissement = LocalDateTime.now().minusDays(7);
            if (demandeModificationDonneesRepository.existsByUtilisateurIdAndDateCreationAfter(
                    utilisateur.getId(), dateRefroidissement)) {
                return ResponseEntity.badRequest()
                    .body("{\"error\": \"Vous avez déjà soumis une demande de modification dans les 7 derniers jours\"}");
            }
            
            DemandeModificationDonnees demande = new DemandeModificationDonnees();
            demande.setUtilisateur(utilisateur);
            demande.setContenu(demandeDto.getContenu());
            demande.setStatut(DemandeModificationDonnees.StatutDemande.ENVOYE);
            
            demande = demandeModificationDonneesRepository.save(demande);
            
            return ResponseEntity.ok()
                .body("{\"message\": \"Demande de modification créée avec succès\", \"id\": \"" + demande.getId() + "\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            log.error("Erreur lors de la création de la demande de modification", e);
            return ResponseEntity.badRequest()
                .body("{\"error\": \"Une erreur s'est produite lors de la création de la demande\"}");
        }
    }
}
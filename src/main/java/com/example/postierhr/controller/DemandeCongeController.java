package com.example.postierhr.controller;

import com.example.postierhr.dto.DemandeCongeDto;
import com.example.postierhr.entity.DemandeConge;
import com.example.postierhr.entity.SoldeConges;
import com.example.postierhr.service.DemandeCongeService;
import com.example.postierhr.service.SoldeCongesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DemandeCongeController {

    private final DemandeCongeService demandeCongeService;
    private final SoldeCongesService soldeCongesService;

    @GetMapping("/leave")
    public String pageConges(Model model) {
        // Créer le solde pour l'année en cours si nécessaire
        SoldeConges solde = soldeCongesService.creerSoldeAnnuelSiNecessaireUtilisateurConnecte();
        model.addAttribute("solde", solde);
        
        List<DemandeConge> demandes = demandeCongeService.listerDemandesUtilisateurConnecte();
        model.addAttribute("demandes", demandes);
        
        model.addAttribute("nouvelleDemande", new DemandeCongeDto());
        return "postier/leave";
    }

    @GetMapping("/api/leave/balance")
    @ResponseBody
    public ResponseEntity<?> obtenirSoldeConges() {
        try {
            Optional<SoldeConges> solde = soldeCongesService.obtenirSoldeAnneeEnCours();
            if (solde.isPresent()) {
                return ResponseEntity.ok(solde.get());
            } else {
                // Créer le solde pour l'année en cours
                SoldeConges nouveauSolde = soldeCongesService.creerSoldeAnnuelSiNecessaireUtilisateurConnecte();
                return ResponseEntity.ok(nouveauSolde);
            }
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du solde de congés", e);
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/api/leave/requests")
    @ResponseBody
    public ResponseEntity<List<DemandeCongeDto>> obtenirMesDemandesConge() {
        try {
            List<DemandeConge> demandes = demandeCongeService.listerDemandesUtilisateurConnecte();
            List<DemandeCongeDto> dtos = demandes.stream().map(demande -> {
                DemandeCongeDto dto = new DemandeCongeDto();
                dto.setId(demande.getId());
                dto.setType(demande.getType());
                dto.setDateDebut(demande.getDateDebut());
                dto.setDateFin(demande.getDateFin());
                dto.setNombreJours(demande.getNombreJours());
                dto.setStatut(demande.getStatut());
                dto.setDateCreation(demande.getDateCreation());
                dto.setUtilisateurNom(demande.getUtilisateur().getNom());
                dto.setUtilisateurPrenom(demande.getUtilisateur().getPrenom());
                return dto;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des demandes de congé", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/leave/requests")
    @ResponseBody
    public ResponseEntity<?> creerDemandeConge(@Valid @RequestBody DemandeCongeDto demandeDto) {
        try {
            DemandeConge demande = demandeCongeService.creerDemande(demandeDto);
            return ResponseEntity.ok().body("{\"message\": \"Demande de congé créée avec succès\", \"id\": \"" + demande.getId() + "\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            log.error("Erreur lors de la création de la demande de congé", e);
            return ResponseEntity.badRequest().body("{\"error\": \"Une erreur s'est produite lors de la création de la demande\"}");
        }
    }
}
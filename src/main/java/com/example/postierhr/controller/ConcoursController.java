package com.example.postierhr.controller;

import com.example.postierhr.dto.ConcoursDto;
import com.example.postierhr.entity.CandidatureConcours;
import com.example.postierhr.entity.Concours;
import com.example.postierhr.entity.Utilisateur;
import com.example.postierhr.service.ConcoursService;
import com.example.postierhr.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ConcoursController {

    private final ConcoursService concoursService;
    private final UtilisateurService utilisateurService;

    @GetMapping("/competitions")
    public String pageCompetitions(@RequestParam(value = "grade", required = false) String grade, Model model) {
        List<Concours> concours;
        if (grade != null && !grade.trim().isEmpty()) {
            concours = concoursService.listerConcoursParGrade(grade);
        } else {
            concours = concoursService.listerTousConcours();
        }
        
        model.addAttribute("concours", concours);
        model.addAttribute("gradeFiltre", grade);
        
        // Récupérer les candidatures de l'utilisateur connecté
        List<CandidatureConcours> candidatures = concoursService.listerCandidaturesUtilisateurConnecte();
        model.addAttribute("candidatures", candidatures);
        
        return "postier/competitions";
    }

    @GetMapping("/api/competitions")
    @ResponseBody
    public ResponseEntity<List<ConcoursDto>> listerConcours(@RequestParam(value = "grade", required = false) String grade) {
        try {
            Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
            List<Concours> concours;
            
            if (grade != null && !grade.trim().isEmpty()) {
                concours = concoursService.listerConcoursParGrade(grade);
            } else {
                concours = concoursService.listerTousConcours();
            }
            
            List<ConcoursDto> dtos = concours.stream().map(c -> {
                ConcoursDto dto = new ConcoursDto();
                dto.setId(c.getId());
                dto.setTitre(c.getTitre());
                dto.setGradeRequis(c.getGradeRequis());
                dto.setDescription(c.getDescription());
                dto.setDateDebutCandidature(c.getDateDebutCandidature());
                dto.setDateFinCandidature(c.getDateFinCandidature());
                dto.setDateCreation(c.getDateCreation());
                dto.setOuvertAuxCandidatures(c.isOuvertAuxCandidatures());
                dto.setUtilisateurDejaCandidat(concoursService.utilisateurDejaCandidat(c.getId()));
                return dto;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des concours", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/competitions/{id}")
    @ResponseBody
    public ResponseEntity<ConcoursDto> obtenirConcours(@PathVariable UUID id) {
        try {
            Optional<Concours> concours = concoursService.obtenirConcours(id);
            if (concours.isPresent()) {
                ConcoursDto dto = new ConcoursDto();
                dto.setId(concours.get().getId());
                dto.setTitre(concours.get().getTitre());
                dto.setGradeRequis(concours.get().getGradeRequis());
                dto.setDescription(concours.get().getDescription());
                dto.setDateDebutCandidature(concours.get().getDateDebutCandidature());
                dto.setDateFinCandidature(concours.get().getDateFinCandidature());
                dto.setDateCreation(concours.get().getDateCreation());
                dto.setOuvertAuxCandidatures(concours.get().isOuvertAuxCandidatures());
                dto.setUtilisateurDejaCandidat(concoursService.utilisateurDejaCandidat(id));
                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du concours", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/competitions/{id}/apply")
    public String postulerConcours(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            CandidatureConcours candidature = concoursService.postulerConcours(id);
            redirectAttributes.addFlashAttribute("successMessage", "Candidature soumise avec succès");
            return "redirect:/competitions";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/competitions";
        } catch (Exception e) {
            log.error("Erreur lors de la candidature au concours", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la candidature");
            return "redirect:/competitions";
        }
    }

    @GetMapping("/api/competitions/applications/mine")
    @ResponseBody
    public ResponseEntity<List<?>> obtenirMesCandidatures() {
        try {
            List<CandidatureConcours> candidatures = concoursService.listerCandidaturesUtilisateurConnecte();
            return ResponseEntity.ok(candidatures);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des candidatures", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
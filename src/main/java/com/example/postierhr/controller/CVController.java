package com.example.postierhr.controller;

import com.example.postierhr.dto.CVDto;
import com.example.postierhr.entity.CV;
import com.example.postierhr.service.CVService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CVController {

    private final CVService cvService;

    @GetMapping("/cv")
    public String pageCv(Model model) {
        Optional<CV> cv = cvService.obtenirCVUtilisateurConnecte();
        if (cv.isPresent()) {
            CVDto cvDto = new CVDto();
            cvDto.setId(cv.get().getId());
            cvDto.setResume(cv.get().getResume());
            cvDto.setCompetences(cv.get().getCompetences());
            cvDto.setFormation(cv.get().getFormation());
            cvDto.setExperiences(cv.get().getExperiences());
            cvDto.setLangues(cv.get().getLangues());
            cvDto.setDerniereModification(cv.get().getDerniereModification());
            model.addAttribute("cv", cvDto);
        } else {
            model.addAttribute("cv", new CVDto());
        }
        model.addAttribute("hasCv", cv.isPresent());
        return "postier/cv";
    }

    @GetMapping("/api/cv/me")
    @ResponseBody
    public ResponseEntity<CVDto> obtenirMonCV() {
        try {
            Optional<CV> cv = cvService.obtenirCVUtilisateurConnecte();
            if (cv.isPresent()) {
                CVDto cvDto = new CVDto();
                cvDto.setId(cv.get().getId());
                cvDto.setResume(cv.get().getResume());
                cvDto.setCompetences(cv.get().getCompetences());
                cvDto.setFormation(cv.get().getFormation());
                cvDto.setExperiences(cv.get().getExperiences());
                cvDto.setLangues(cv.get().getLangues());
                cvDto.setDerniereModification(cv.get().getDerniereModification());
                return ResponseEntity.ok(cvDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du CV", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/api/cv/me")
    @ResponseBody
    public ResponseEntity<?> creerOuMettreAJourCV(@Valid @RequestBody CVDto cvDto) {
        try {
            CV cv = cvService.creerOuMettreAJourCV(cvDto);
            return ResponseEntity.ok().body("{\"message\": \"CV sauvegardé avec succès\", \"id\": \"" + cv.getId() + "\"}");
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde du CV", e);
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/api/cv/me")
    @ResponseBody
    public ResponseEntity<?> supprimerCV() {
        try {
            cvService.supprimerCV();
            return ResponseEntity.ok().body("{\"message\": \"CV supprimé avec succès\"}");
        } catch (Exception e) {
            log.error("Erreur lors de la suppression du CV", e);
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/api/cv/print")
    @ResponseBody
    public ResponseEntity<?> imprimerCV() {
        try {
            // TODO: Implémenter la génération PDF
            return ResponseEntity.ok().body("{\"message\": \"Génération PDF non encore implémentée\"}");
        } catch (Exception e) {
            log.error("Erreur lors de l'impression du CV", e);
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
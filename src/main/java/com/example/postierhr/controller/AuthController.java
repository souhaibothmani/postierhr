package com.example.postierhr.controller;

import com.example.postierhr.dto.UtilisateurDto;
import com.example.postierhr.entity.Utilisateur;
import com.example.postierhr.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UtilisateurService utilisateurService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String accueil() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String pageConnexion(@RequestParam(value = "error", required = false) String erreur,
                               @RequestParam(value = "logout", required = false) String deconnexion,
                               Model model) {
        if (erreur != null) {
            model.addAttribute("erreur", "Matricule ou mot de passe incorrect");
        }
        if (deconnexion != null) {
            model.addAttribute("message", "Vous avez été déconnecté avec succès");
        }
        return "public/login";
    }

    @GetMapping("/api/auth/me")
    @ResponseBody
    public ResponseEntity<UtilisateurDto> obtenirUtilisateurConnecte(Authentication authentication) {
        try {
            Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
            UtilisateurDto dto = new UtilisateurDto();
            dto.setId(utilisateur.getId());
            dto.setMatricule(utilisateur.getMatricule());
            dto.setEmail(utilisateur.getEmail());
            dto.setPrenom(utilisateur.getPrenom());
            dto.setNom(utilisateur.getNom());
            dto.setGrade(utilisateur.getGrade());
            dto.setBureau(utilisateur.getBureau());
            dto.setDepartement(utilisateur.getDepartement());
            dto.setActif(utilisateur.getActif());
            dto.setDateCreation(utilisateur.getDateCreation());
            dto.setDateModification(utilisateur.getDateModification());
            
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de l'utilisateur connecté", e);
            return ResponseEntity.badRequest().build();
        }
    }

}
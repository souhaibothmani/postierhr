package com.example.postierhr.controller;

import com.example.postierhr.entity.Utilisateur;
import com.example.postierhr.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TableauDeBordController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    public String tableauDeBord(Model model) {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        model.addAttribute("utilisateur", utilisateur);
        return "postier/dashboard";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String monProfil(Model model) {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        model.addAttribute("utilisateur", utilisateur);
        return "postier/profile";
    }
}
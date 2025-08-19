package com.example.postierhr.service;

import com.example.postierhr.dto.CVDto;
import com.example.postierhr.entity.CV;
import com.example.postierhr.entity.Utilisateur;
import com.example.postierhr.repository.CVRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CVService {

    private final CVRepository cvRepository;
    private final UtilisateurService utilisateurService;

    @Transactional(readOnly = true)
    public Optional<CV> obtenirCVParUtilisateur(Utilisateur utilisateur) {
        return cvRepository.findByUtilisateur(utilisateur);
    }

    @Transactional(readOnly = true)
    public Optional<CV> obtenirCVUtilisateurConnecte() {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        return obtenirCVParUtilisateur(utilisateur);
    }

    @Transactional(readOnly = true)
    public boolean utilisateurACv(UUID utilisateurId) {
        return cvRepository.existsByUtilisateurId(utilisateurId);
    }

    @Transactional(readOnly = true)
    public boolean utilisateurConnecteACv() {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        return cvRepository.existsByUtilisateur(utilisateur);
    }

    public CV creerOuMettreAJourCV(CVDto cvDto) {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        
        CV cv = cvRepository.findByUtilisateur(utilisateur)
            .orElse(new CV());
        
        cv.setUtilisateur(utilisateur);
        cv.setResume(cvDto.getResume());
        cv.setCompetences(cvDto.getCompetences());
        cv.setFormation(cvDto.getFormation());
        cv.setExperiences(cvDto.getExperiences());
        cv.setLangues(cvDto.getLangues());
        
        return cvRepository.save(cv);
    }

    public void supprimerCV() {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        cvRepository.findByUtilisateur(utilisateur)
            .ifPresent(cvRepository::delete);
    }

    @Transactional(readOnly = true)
    public CV obtenirCVPourImpression(UUID cvId) {
        CV cv = cvRepository.findById(cvId)
            .orElseThrow(() -> new IllegalArgumentException("CV non trouvé"));
        
        Utilisateur utilisateurConnecte = utilisateurService.obtenirUtilisateurConnecte();
        if (!cv.getUtilisateur().getId().equals(utilisateurConnecte.getId())) {
            throw new IllegalArgumentException("Vous n'avez pas l'autorisation d'imprimer ce CV");
        }
        
        return cv;
    }
}
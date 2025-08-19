package com.example.postierhr.service;

import com.example.postierhr.entity.Utilisateur;
import com.example.postierhr.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public Utilisateur creerUtilisateur(String matricule, String email, String motDePasse, 
                                       String prenom, String nom, String grade, String bureau, String departement) {
        if (utilisateurRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }
        if (utilisateurRepository.existsByMatricule(matricule)) {
            throw new IllegalArgumentException("Un utilisateur avec ce matricule existe déjà");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMatricule(matricule);
        utilisateur.setEmail(email);
        utilisateur.setMotDePasseHash(passwordEncoder.encode(motDePasse));
        utilisateur.setPrenom(prenom);
        utilisateur.setNom(nom);
        utilisateur.setGrade(grade);
        utilisateur.setBureau(bureau);
        utilisateur.setDepartement(departement);
        utilisateur.setActif(true);

        return utilisateurRepository.save(utilisateur);
    }

    @Transactional(readOnly = true)
    public Optional<Utilisateur> trouverParEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Utilisateur> trouverParId(UUID id) {
        return utilisateurRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Utilisateur> trouverParMatricule(String matricule) {
        return utilisateurRepository.findByMatricule(matricule);
    }

    @Transactional(readOnly = true)
    public Utilisateur obtenirUtilisateurConnecte() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Aucun utilisateur connecté");
        }
        
        String matricule = authentication.getName();
        return trouverParMatricule(matricule)
            .orElseThrow(() -> new IllegalStateException("Utilisateur connecté non trouvé: " + matricule));
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> listerTousLesUtilisateursActifs() {
        return utilisateurRepository.findByActifTrueOrderByNomAscPrenomAsc();
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> listerUtilisateursParGrade(String grade) {
        return utilisateurRepository.findByGradeAndActifTrueOrderByNomAscPrenomAsc(grade);
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> rechercherUtilisateurs(String terme) {
        if (terme == null || terme.trim().isEmpty()) {
            return listerTousLesUtilisateursActifs();
        }
        return utilisateurRepository.rechercherUtilisateurs(terme.trim());
    }

    public void mettreAJourMotDePasse(UUID utilisateurId, String nouveauMotDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        utilisateur.setMotDePasseHash(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(utilisateur);
    }

    public void desactiverUtilisateur(UUID utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        utilisateur.setActif(false);
        utilisateurRepository.save(utilisateur);
    }

}
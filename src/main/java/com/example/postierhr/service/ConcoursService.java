package com.example.postierhr.service;

import com.example.postierhr.entity.CandidatureConcours;
import com.example.postierhr.entity.Concours;
import com.example.postierhr.entity.Utilisateur;
import com.example.postierhr.repository.CandidatureConcoursRepository;
import com.example.postierhr.repository.ConcoursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ConcoursService {

    private final ConcoursRepository concoursRepository;
    private final CandidatureConcoursRepository candidatureConcoursRepository;
    private final UtilisateurService utilisateurService;
    private final CVService cvService;

    @Transactional(readOnly = true)
    public List<Concours> listerTousConcours() {
        return concoursRepository.findAllByOrderByDateCreationDesc();
    }

    @Transactional(readOnly = true)
    public List<Concours> listerConcoursOuverts() {
        return concoursRepository.findConcoursOuverts(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<Concours> listerConcoursParGrade(String grade) {
        if (grade == null || grade.trim().isEmpty()) {
            return listerTousConcours();
        }
        return concoursRepository.findByGradeRequisOrNull(grade);
    }

    @Transactional(readOnly = true)
    public List<Concours> listerConcoursOuvertsParGrade(String grade) {
        if (grade == null || grade.trim().isEmpty()) {
            return listerConcoursOuverts();
        }
        return concoursRepository.findConcoursOuvertsParGrade(grade, LocalDate.now());
    }

    @Transactional(readOnly = true)
    public Optional<Concours> obtenirConcours(UUID concoursId) {
        return concoursRepository.findById(concoursId);
    }

    @Transactional(readOnly = true)
    public List<CandidatureConcours> listerCandidaturesUtilisateurConnecte() {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        return candidatureConcoursRepository.findByUtilisateurOrderByDateCreationDesc(utilisateur);
    }

    @Transactional(readOnly = true)
    public boolean utilisateurDejaCandidat(UUID concoursId) {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        return candidatureConcoursRepository.existsByUtilisateurIdAndConcoursId(utilisateur.getId(), concoursId);
    }

    public CandidatureConcours postulerConcours(UUID concoursId) {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        
        // Vérifier que l'utilisateur a un CV
        if (!cvService.utilisateurConnecteACv()) {
            throw new IllegalArgumentException("Vous devez avoir un CV pour postuler à un concours");
        }
        
        // Vérifier que le concours existe
        Concours concours = concoursRepository.findById(concoursId)
            .orElseThrow(() -> new IllegalArgumentException("Concours non trouvé"));
        
        // Vérifier que les candidatures sont ouvertes
        if (!concours.isOuvertAuxCandidatures()) {
            throw new IllegalArgumentException("Les candidatures ne sont pas ouvertes pour ce concours");
        }
        
        // Vérifier que l'utilisateur n'a pas déjà postulé
        if (candidatureConcoursRepository.existsByUtilisateurAndConcours(utilisateur, concours)) {
            throw new IllegalArgumentException("Vous avez déjà postulé à ce concours");
        }
        
        // Vérifier le grade requis si spécifié
        if (concours.getGradeRequis() != null && !concours.getGradeRequis().isEmpty()) {
            if (utilisateur.getGrade() == null || !utilisateur.getGrade().equals(concours.getGradeRequis())) {
                throw new IllegalArgumentException("Votre grade ne correspond pas aux exigences de ce concours");
            }
        }
        
        CandidatureConcours candidature = new CandidatureConcours();
        candidature.setUtilisateur(utilisateur);
        candidature.setConcours(concours);
        
        return candidatureConcoursRepository.save(candidature);
    }
}
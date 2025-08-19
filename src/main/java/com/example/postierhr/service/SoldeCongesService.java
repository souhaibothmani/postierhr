package com.example.postierhr.service;

import com.example.postierhr.entity.SoldeConges;
import com.example.postierhr.entity.Utilisateur;
import com.example.postierhr.repository.SoldeCongesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SoldeCongesService {

    private final SoldeCongesRepository soldeCongesRepository;
    private final UtilisateurService utilisateurService;

    @Transactional(readOnly = true)
    public Optional<SoldeConges> obtenirSoldeAnneeEnCours() {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        int anneeEnCours = LocalDate.now().getYear();
        return soldeCongesRepository.findByUtilisateurAndAnnee(utilisateur, anneeEnCours);
    }

    @Transactional(readOnly = true)
    public List<SoldeConges> obtenirHistoriqueSoldes() {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        return soldeCongesRepository.findByUtilisateurOrderByAnneeDesc(utilisateur);
    }

    public SoldeConges creerSoldeAnnuelSiNecessaire(Utilisateur utilisateur, int annee) {
        Optional<SoldeConges> soldeExistant = soldeCongesRepository.findByUtilisateurAndAnnee(utilisateur, annee);
        
        if (soldeExistant.isPresent()) {
            return soldeExistant.get();
        }
        
        SoldeConges nouveauSolde = new SoldeConges();
        nouveauSolde.setUtilisateur(utilisateur);
        nouveauSolde.setAnnee(annee);
        nouveauSolde.setJoursAnnuels(BigDecimal.valueOf(25.0)); // 25 jours par défaut
        nouveauSolde.setJoursMaladie(BigDecimal.valueOf(30.0)); // 30 jours par défaut
        
        return soldeCongesRepository.save(nouveauSolde);
    }

    public SoldeConges creerSoldeAnnuelSiNecessaireUtilisateurConnecte() {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        int anneeEnCours = LocalDate.now().getYear();
        return creerSoldeAnnuelSiNecessaire(utilisateur, anneeEnCours);
    }
}
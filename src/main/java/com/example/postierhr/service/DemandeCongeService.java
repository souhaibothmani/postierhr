package com.example.postierhr.service;

import com.example.postierhr.dto.DemandeCongeDto;
import com.example.postierhr.entity.DemandeConge;
import com.example.postierhr.entity.Utilisateur;
import com.example.postierhr.repository.DemandeCongeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DemandeCongeService {

    private final DemandeCongeRepository demandeCongeRepository;
    private final UtilisateurService utilisateurService;

    @Transactional(readOnly = true)
    public List<DemandeConge> listerDemandesParUtilisateur(Utilisateur utilisateur) {
        return demandeCongeRepository.findByUtilisateurOrderByDateCreationDesc(utilisateur);
    }

    @Transactional(readOnly = true)
    public List<DemandeConge> listerDemandesUtilisateurConnecte() {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        return listerDemandesParUtilisateur(utilisateur);
    }

    public DemandeConge creerDemande(DemandeCongeDto demandeDto) {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurConnecte();
        
        // Vérifier la période de refroidissement (7 jours)
        LocalDateTime dateRefroidissement = LocalDateTime.now().minusDays(7);
        if (demandeCongeRepository.existsByUtilisateurIdAndDateCreationAfter(utilisateur.getId(), dateRefroidissement)) {
            throw new IllegalArgumentException("Vous avez déjà soumis une demande de congé dans les 7 derniers jours");
        }
        
        // Vérifier les conflits de dates
        List<DemandeConge> conflits = demandeCongeRepository.findCongesEnConflit(
            utilisateur.getId(), 
            demandeDto.getDateDebut(), 
            demandeDto.getDateFin()
        );
        
        if (!conflits.isEmpty()) {
            throw new IllegalArgumentException("Des congés sont déjà prévus sur cette période");
        }
        
        // Valider les dates
        if (demandeDto.getDateFin().isBefore(demandeDto.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début");
        }
        
        DemandeConge demande = new DemandeConge();
        demande.setUtilisateur(utilisateur);
        demande.setType(demandeDto.getType());
        demande.setDateDebut(demandeDto.getDateDebut());
        demande.setDateFin(demandeDto.getDateFin());
        demande.setNombreJours(demandeDto.getNombreJours());
        demande.setStatut(DemandeConge.StatutDemande.ENVOYE);
        
        return demandeCongeRepository.save(demande);
    }
}
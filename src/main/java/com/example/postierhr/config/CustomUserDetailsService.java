package com.example.postierhr.config;

import com.example.postierhr.entity.Utilisateur;
import com.example.postierhr.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String matricule) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByMatricule(matricule)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le matricule: " + matricule));

        if (!utilisateur.getActif()) {
            throw new UsernameNotFoundException("Compte désactivé pour le matricule: " + matricule);
        }

        log.debug("Loading user {} with password hash: {}", matricule, utilisateur.getMotDePasseHash());

        return User.builder()
            .username(utilisateur.getMatricule())
            .password(utilisateur.getMotDePasseHash())
            .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_POSTIER")))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!utilisateur.getActif())
            .build();
    }
}
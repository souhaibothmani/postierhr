package com.example.postierhr.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InscriptionDto {

    @NotBlank(message = "Le matricule est obligatoire")
    @Size(max = 20, message = "Le matricule ne peut pas dépasser 20 caractères")
    private String matricule;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;

    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    private String confirmationMotDePasse;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100, message = "Le prénom ne peut pas dépasser 100 caractères")
    private String prenom;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    private String nom;

    @Size(max = 50, message = "Le grade ne peut pas dépasser 50 caractères")
    private String grade;

    public boolean motDePasseValide() {
        return motDePasse != null && motDePasse.equals(confirmationMotDePasse);
    }
}
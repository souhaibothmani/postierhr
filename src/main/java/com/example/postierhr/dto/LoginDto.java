package com.example.postierhr.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    
    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;
}
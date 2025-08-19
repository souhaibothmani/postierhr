package com.example.postierhr.dto;

import com.example.postierhr.entity.Document;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DocumentDto {
    
    private UUID id;
    private String nomFichier;
    private String nomOriginal;
    private String typeMime;
    private Long taille;
    private String cheminFichier;
    private LocalDateTime dateCreation;
    private Document.TypeDocument typeDocument;
    
    // Informations de l'utilisateur qui a uploadé
    private String utilisateurNom;
    private String utilisateurPrenom;
    
    // Méthodes utilitaires
    public String getTailleFormatee() {
        if (taille == null) return "0 B";
        
        String[] units = {"B", "KB", "MB", "GB"};
        double size = taille;
        int unitIndex = 0;
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.1f %s", size, units[unitIndex]);
    }
    
    public boolean isImage() {
        return typeMime != null && typeMime.startsWith("image/");
    }
    
    public boolean isPdf() {
        return "application/pdf".equals(typeMime);
    }
    
    public boolean isWord() {
        return typeMime != null && (
            typeMime.equals("application/msword") ||
            typeMime.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        );
    }
    
    public String getIconClass() {
        if (isPdf()) return "bi-file-earmark-pdf text-danger";
        if (isWord()) return "bi-file-earmark-word text-primary";
        if (isImage()) return "bi-file-earmark-image text-success";
        return "bi-file-earmark text-secondary";
    }
}
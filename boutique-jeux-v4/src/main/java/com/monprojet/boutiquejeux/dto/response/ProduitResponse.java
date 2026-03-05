package com.monprojet.boutiquejeux.dto.response;
import java.math.BigDecimal;
import java.util.List;
public record ProduitResponse(
    Long id, String nom, String description, String plateforme,
    Integer pegi, String categorie, String typeCategorie,
    String niveauAccesMin, Boolean necessiteNumeroSerie,
    BigDecimal prixNeuf, BigDecimal prixOccasion,
    String imagePrincipaleUrl, String imagePrincipaleAlt,
    List<ImageResponse> images,
    Double noteMoyenne, Long nbAvis
) {}

package com.monprojet.boutiquejeux.dto.response;
import java.time.LocalDateTime;
public record AvisResponse(Long id, String auteur, Integer note, String commentaire, LocalDateTime dateCreation) {}

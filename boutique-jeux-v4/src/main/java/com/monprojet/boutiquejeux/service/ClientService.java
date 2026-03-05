package com.monprojet.boutiquejeux.service;
import com.monprojet.boutiquejeux.dto.response.ClientResponse;
import com.monprojet.boutiquejeux.entity.Client;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
@Service @RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    @Transactional(readOnly = true)
    public ClientResponse findById(Long id) {
        Client c = clientRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));
        return new ClientResponse(c.getId(), c.getPseudo(), c.getNom(), c.getPrenom(), c.getEmail(), c.getTelephone(), c.getDateNaissance(), c.getTypeFidelite().getCode(), c.getNumeroCarteFidelite(), c.getRgpdConsent(), c.getDateCreation());
    }
    @Transactional
    public void demanderSuppression(Long clientId) {
        Client c = clientRepository.findByIdAndDeletedFalse(clientId).orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));
        c.setDemandeSuppression(true);
        c.setDateSuppression(LocalDateTime.now());
        clientRepository.save(c);
    }
}

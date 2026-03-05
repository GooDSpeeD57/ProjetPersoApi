package com.monprojet.boutiquejeux.config;

import com.monprojet.boutiquejeux.entity.Client;
import com.monprojet.boutiquejeux.entity.Employe;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.ClientRepository;
import com.monprojet.boutiquejeux.repository.EmployeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityHelper {

    private final ClientRepository clientRepository;
    private final EmployeRepository employeRepository;

    public Client getClientCourant(UserDetails userDetails) {
        return clientRepository.findByEmailAndDeletedFalse(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé"));
    }

    public Employe getEmployeCourant(UserDetails userDetails) {
        return employeRepository.findByEmailAndDeletedFalse(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));
    }
}

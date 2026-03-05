package com.monprojet.boutiquejeux.security.service;

import com.monprojet.boutiquejeux.entity.Employe;
import com.monprojet.boutiquejeux.repository.EmployeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeUserDetailsService implements UserDetailsService {

    private final EmployeRepository employeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employe employe = employeRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employé non trouvé : " + email));

        String role = "ROLE_" + employe.getRole().getCode(); // ROLE_VENDEUR / ROLE_MANAGER / ROLE_ADMIN
        return User.builder()
                .username(employe.getEmail())
                .password(employe.getMotDePasse())
                .authorities(List.of(new SimpleGrantedAuthority(role)))
                .build();
    }
}

package com.monprojet.boutiquejeux.security.service;

import com.monprojet.boutiquejeux.entity.Client;
import com.monprojet.boutiquejeux.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new UsernameNotFoundException("Client non trouvé : " + email));

        String role = "ROLE_" + client.getTypeFidelite().getCode(); // ROLE_NORMAL / ROLE_PREMIUM / ROLE_ULTIMATE
        return User.builder()
                .username(client.getEmail())
                .password(client.getMotDePasse())
                .authorities(List.of(new SimpleGrantedAuthority(role)))
                .build();
    }
}

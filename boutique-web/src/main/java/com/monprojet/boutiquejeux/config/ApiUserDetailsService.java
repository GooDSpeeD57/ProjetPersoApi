package com.monprojet.boutiquejeux.config;

import com.monprojet.boutiquejeux.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Spring Security appelle loadUserByUsername() avec l'email saisi.
 * On appelle l'API pour vérifier les credentials et récupérer le JWT.
 * Le mot de passe retourné est un placeholder car c'est l'API qui valide.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiUserDetailsService implements UserDetailsService {

    private final ApiService apiService;

    // Stocké en ThreadLocal pour le passer depuis AuthenticationProvider
    static final ThreadLocal<String> CURRENT_PASSWORD = new ThreadLocal<>();
    static final ThreadLocal<String> CURRENT_USER_TYPE = new ThreadLocal<>();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String motDePasse = CURRENT_PASSWORD.get();
        String userType   = CURRENT_USER_TYPE.get() != null ? CURRENT_USER_TYPE.get() : "CLIENT";

        try {
            Map<String, Object> resp = apiService.login(email, motDePasse, userType);
            if (resp == null || resp.get("accessToken") == null)
                throw new UsernameNotFoundException("Réponse API invalide");

            // Stocker le JWT en session
            ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpSession session = attrs.getRequest().getSession();
                session.setAttribute("jwt",       resp.get("accessToken"));
                session.setAttribute("userEmail", resp.get("email"));
                session.setAttribute("userRole",  resp.get("role"));
            }

            String role = String.valueOf(resp.get("role"));
            // Normaliser : ROLE_CLIENT, ROLE_ADMIN, etc.
            String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;

            return User.builder()
                    .username(email)
                    .password("{noop}" + motDePasse) // Spring Security ne re-vérifie pas, l'API l'a fait
                    .authorities(List.of(new SimpleGrantedAuthority(authority)))
                    .build();

        } catch (RuntimeException e) {
            log.warn("Login API échoué pour {} : {}", email, e.getMessage());
            throw new UsernameNotFoundException("Identifiants invalides");
        } finally {
            CURRENT_PASSWORD.remove();
            CURRENT_USER_TYPE.remove();
        }
    }
}

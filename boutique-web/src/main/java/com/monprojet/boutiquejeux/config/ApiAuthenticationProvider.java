package com.monprojet.boutiquejeux.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Intercepte l'authentication avant Spring Security pour passer
 * le mot de passe en clair au UserDetailsService (qui l'envoie à l'API).
 * Récupère aussi le champ "userType" du formulaire de login.
 */
@Component
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {

    private final ApiUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String email    = auth.getName();
        String password = auth.getCredentials().toString();

        // Récupérer userType depuis le formulaire
        String userType = "CLIENT";
        ServletRequestAttributes attrs =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest req = attrs.getRequest();
            String ut = req.getParameter("userType");
            if (ut != null && !ut.isBlank()) userType = ut;
        }

        // Transmettre au UserDetailsService via ThreadLocal
        ApiUserDetailsService.CURRENT_PASSWORD.set(password);
        ApiUserDetailsService.CURRENT_USER_TYPE.set(userType);

        UserDetails user = userDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> auth) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(auth);
    }
}

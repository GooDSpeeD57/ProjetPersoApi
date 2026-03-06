package com.monprojet.boutiquejeux.util;

/**
 * Singleton thread-local pour stocker la session de l'employé connecté.
 */
public class SessionManager {

    private static SessionManager instance;

    private String jwt;
    private String email;
    private String role;       // ROLE_VENDEUR / ROLE_MANAGER / ROLE_ADMIN
    private String prenom;
    private Long   magasinId;
    private String magasinNom;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    public void login(String jwt, String email, String role) {
        this.jwt   = jwt;
        this.email = email;
        this.role  = role;
    }

    public void setInfos(String prenom, Long magasinId, String magasinNom) {
        this.prenom     = prenom;
        this.magasinId  = magasinId;
        this.magasinNom = magasinNom;
    }

    public void logout() {
        this.jwt        = null;
        this.email      = null;
        this.role       = null;
        this.prenom     = null;
        this.magasinId  = null;
        this.magasinNom = null;
    }

    public boolean isLoggedIn()   { return jwt != null; }
    public boolean isAdmin()      { return "ROLE_ADMIN".equals(role); }
    public boolean isManager()    { return "ROLE_ADMIN".equals(role) || "ROLE_MANAGER".equals(role); }

    public String  getJwt()       { return jwt; }
    public String  getEmail()     { return email; }
    public String  getRole()      { return role; }
    public String  getPrenom()    { return prenom != null ? prenom : email; }
    public Long    getMagasinId() { return magasinId; }
    public String  getMagasinNom(){ return magasinNom; }

    public String  getBearerToken() { return "Bearer " + jwt; }
}

# BoutiqueJeux — Application Employé (JavaFX 23)

Application desktop pour les employés Micromania, connectée à l'API Spring Boot.

## Prérequis

- **Java 21** (JDK) — `java -version`
- **Maven 3.9+** — `mvn -version`
- **API Spring Boot** démarrée sur `http://localhost:8080`

> ⚠️ JavaFX 23 est inclus via Maven, pas besoin de l'installer séparément.

## Lancement

```bash
mvn javafx:run
```

## Build en JAR exécutable

```bash
mvn clean package javafx:jlink
# Exécutable dans target/boutique-javafx/bin/boutique-javafx
```

## Configuration API

Modifier `ApiClient.java` pour changer l'URL de l'API :
```java
private static final String BASE_URL = "http://localhost:8080/api";
```

## Fonctionnalités

| Module | Description |
|---|---|
| 🔐 Login | Connexion employé (ROLE_VENDEUR / MANAGER / ADMIN) |
| 🏠 Dashboard | Stats du jour : ventes, CA, reprises, stocks bas |
| 👤 Créer client | Création compte + 10 pts de bienvenue automatiques |
| 🛒 Vente | Catalogue neuf/occasion, panier, paiement, points fidélité |
| ♻️ Reprise | Estimation par barème état, validation avec avoir |
| 📦 Stock | Consultation + mise à jour quantités, alertes stock bas |
| 🛡️ Garanties | Consultation garanties par client (actives/expirées) |
| 🔍 Recherche | Recherche universelle client ou produit |
| 📅 Planning | Vue planning équipe par semaine (managers) |

## Structure du projet

```
src/main/java/com/monprojet/boutiquejeux/
├── MainApp.java                    ← Point d'entrée JavaFX
├── controller/
│   ├── LoginController.java
│   ├── MainController.java
│   ├── DashboardController.java
│   ├── CreerClientController.java
│   ├── VenteController.java
│   ├── RepriseController.java
│   ├── StockController.java
│   ├── GarantiesController.java
│   ├── RechercheController.java
│   └── PlanningController.java
├── service/
│   └── ApiClient.java              ← HttpClient Java 11 natif
├── model/
│   └── LignePanier.java
└── util/
    ├── SessionManager.java         ← JWT en mémoire
    └── AlertHelper.java

src/main/resources/
├── fxml/          ← Vues FXML
└── css/
    └── dark-theme.css              ← Thème sombre gaming
```

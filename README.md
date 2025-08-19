# Site RH pour Postiers

Une application web Spring Boot pour la gestion des ressources humaines des postiers, avec authentification par session et interface en français.

## 🚀 Fonctionnalités

### Pour les Postiers
- **Authentification** : Connexion/déconnexion sécurisée avec sessions
- **Profil** : Consultation et modification des données personnelles (avec validation admin)
- **CV** : Création, modification, suppression et export PDF du CV
- **Demandes** : 
  - Attestations (travail, salaire)
  - Congés (avec gestion des soldes et historique)
- **Concours** : Consultation, candidature et suivi des concours internes

### Pour les Administrateurs
- **Validation** : Approbation/rejet des demandes
- **Gestion** : Administration des utilisateurs et concours
- **Reporting** : Suivi des activités et statistiques

## 🛠 Stack Technique

- **Backend** : Java 21, Spring Boot 3.3.5
- **Sécurité** : Spring Security (formulaires + sessions JSESSIONID)
- **Base de données** : PostgreSQL 15+
- **Frontend** : HTML, CSS, JavaScript (Bootstrap 5, Thymeleaf)
- **Build** : Gradle avec Kotlin DSL
- **Tests** : JUnit 5, Testcontainers
- **DevOps** : Docker Compose

## 📋 Prérequis

- Java 21+
- Docker et Docker Compose
- Git

## 🚀 Installation et Démarrage

### 1. Cloner le projet
```bash
git clone <repository-url>
cd postierhr
```

### 2. Démarrer la base de données
```bash
docker-compose up -d postgres
```

### 3. Construire et lancer l'application
```bash
./gradlew bootRun
```

L'application sera accessible sur : http://localhost:8080

### 4. Accès PgAdmin (optionnel)
- URL : http://localhost:8081
- Email : admin@postierhr.local
- Mot de passe : admin123

## 👥 Comptes de Test

Mot de passe pour tous les comptes : `password123`

### Administrateur
- Email : admin@laposte.fr
- Rôle : Administration complète

### Postiers
- Email : jean.martin@laposte.fr (Facteur)
- Email : sophie.bernard@laposte.fr (Conseiller financier)
- Email : pierre.durand@laposte.fr (Chef d'équipe)

## 🗂 Structure du Projet

```
src/
├── main/
│   ├── java/com/example/postierhr/
│   │   ├── config/          # Configuration Spring Security
│   │   ├── controller/      # Contrôleurs MVC et REST
│   │   ├── dto/            # Objets de transfert de données
│   │   ├── entity/         # Entités JPA
│   │   ├── mapper/         # Mappers MapStruct
│   │   ├── repository/     # Repositories Spring Data
│   │   ├── service/        # Services métier
│   │   └── exception/      # Exceptions personnalisées
│   └── resources/
│       ├── static/         # CSS, JS, images
│       └── templates/      # Templates Thymeleaf
└── test/                   # Tests unitaires et d'intégration
```

## 🗄 Modèle de Données

### Entités principales
- **Utilisateur** : Informations de base et authentification
- **DonneesPersonnelles** : Détails personnels des utilisateurs
- **CV** : Curriculum vitae complet
- **DemandeAttestation** : Demandes d'attestations
- **DemandeConge** : Demandes de congé
- **SoldeConges** : Soldes de congés par année
- **Concours** : Concours internes
- **CandidatureConcours** : Candidatures aux concours

## 🔒 Sécurité

- **Authentification** : Formulaire avec sessions HTTP
- **Autorisation** : Contrôle d'accès basé sur les rôles (POSTIER/ADMIN)
- **Protection CSRF** : Activée pour les formulaires
- **Hachage** : Mots de passe avec BCrypt
- **Sessions** : Cookie JSESSIONID sécurisé

## 🧪 Tests

```bash
# Tests unitaires
./gradlew test

# Tests d'intégration avec Testcontainers
./gradlew integrationTest

# Tous les tests
./gradlew check
```

## 📚 Documentation API

Les endpoints REST sont documentés et accessibles via :
- `/api/docs` - Documentation interactive
- `/api/health` - Health check

## 🐳 Docker

### Développement local
```bash
docker-compose up -d
```

### Production
```bash
docker-compose -f docker-compose.prod.yml up -d
```

## 🔄 Migrations

Le schéma de base de données est automatiquement géré par Hibernate (JPA).

## 📝 Configuration

### Profils disponibles
- **local** : Développement local (par défaut)
- **test** : Tests automatisés
- **prod** : Production

### Variables d'environnement principales
```properties
SPRING_PROFILES_ACTIVE=local
DATABASE_URL=jdbc:postgresql://localhost:5432/postierhr
DATABASE_USER=postierhr
DATABASE_PASSWORD=postierhr
```

## 🤝 Contribution

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/nouvelle-fonctionnalite`)
3. Commiter les changements (`git commit -am 'Ajout nouvelle fonctionnalité'`)
4. Pousser vers la branche (`git push origin feature/nouvelle-fonctionnalite`)
5. Créer une Pull Request

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## 🆘 Support

Pour toute question ou problème :
1. Consulter la documentation
2. Vérifier les issues existantes
3. Créer une nouvelle issue avec les détails

## 🔄 Roadmap

- [ ] Notifications en temps réel
- [ ] API mobile
- [ ] Intégration calendrier
- [ ] Reporting avancé
- [ ] Export Excel
- [ ] Workflow de validation configurables
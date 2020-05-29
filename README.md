# Secure microservice with Keycloak

Comment faire pour sécuriser votre microservice developper avec spring boot ? Pour cela nous allons utilisé keycloak. 
![enter image description here](https://lh3.googleusercontent.com/Qyy_ZtVR9_maK04ld8FYJ1-IxlFx143wViAkrmMFxMzraxNXCShuvYM0wMZO8Tbhg36EZVtAOyZjfnT4rRdw5OQTKE0V_-Cio3YFitrNkT6rFSgTCjortkbEwWlwDlcYqKgPeywiELyUwRgTeJ6TDeSLkh34hCunGhdt8JuDK21C4cBSvuEGRvJzEgfVscQjv3HD1PvmWn8ill7pPosp9a4Ajqkw6O4HhvEcpQjZ_k9ueS8hSvERhOVRhBocsfL4gG01owcHWZzBOlRcLVwpWcMizTK1ZfcMprJpqOctiXOxsUf9KKiyCyvmM9EiugSX78lDIE6k6bDoMwMz55dQHZBzrRfelxNqCB4HAJO3qGCssEdR2tuuW-R8G-S-1AWs4plUkClNvBsDZDXh9Z55TeZlaN-AZPX2s1_tIL6iylXLFnH5pxKuTy68UvqvXYjre-Nv1UnHeChIYA2i749AP2TI1SFB1eKwqAAGLKNQdrVqB_n6Wp0H9GAJ-EKIiYROyuZpsLyym1i-kF2VEshBcsaWhs3PxkFIW31V71pkEwoGK2V-jGks55UyFe4Juphfh18siB9G6foiVgyEpl8IlYy2Tzl7Bassb74YrQK3GZud6F0R3Fl8vm8UggfPB4vpVRYyF2XuvS2nIQKgELk6UShjdXG0RbU53Jz6h-UFBGoD3Oe-LnAaBXMhvZkQ4-k=w1259-h708-no?authuser=0)

**Keycloak** est un produit logiciel open source permettant la connexion unique avec la gestion des identités "Identity management"  et la gestion des accès "Gestion des accès" destinées aux applications et services modernes. Depuis mars 2018, ce projet communautaire JBoss est sous la direction de Red Hat, qui l'utilise comme projet en amont (développement logiciel)") pour leur produit RH-SSO.


# Installation et démarrage keycloak

Tres facile, pour installer keycloak il suffit de telecharger le zip de la version standalone sur le [site web de keycloak]("[https://www.keycloak.org/downloads](https://www.keycloak.org/downloads)"). Decompresser le fichier sur votre machine et positionner vous sur le repertoire /bin puis executer l'application 
> keycloak-10.0.1/bin/standalone.bat
> 
Apres le demarrage du service, acceder a l'interface via 

> http://votreHost:8080 dans notre cas nous sommes en localhost

- Créer un profil Admin
- Créer votre application a sécurisée
- Créer un ou plusieurs utilisateurs 
- Créer les roles de votre choix
- Affecter a chaque utilisateur un rôle

## MicroService SpringBoot

Ici nous supposons que vous avez déjà créer votre micro-service. Pour notre cas le micro-service permet d’accéder a la ressource suivante 
> http://localhost:8081/contributions

1. Dans notre code nous allons prendre la précaution d'exposer les ID des contribution avec Spring Data.
> @Autowired  
private RepositoryRestConfiguration restConfiguration;
.
public void run(String... args) throws Exception {
	restConfiguration.exposeIdsFor(Contribution.class);
}

2. Nous allons ajouter les dépendances **keycloak** et **spring security** nécessaires dans le pom.xml de notre micro-service.
> * keycloak-spring-boot-starter
> * spring-boot-starter-security

3. Maintenant créons la classe de configuration keycloak
> @Configuration  
class keycloakConfig{  
   @Bean  
  KeycloakSpringBootConfigResolver configResolver(){  
      return new KeycloakSpringBootConfigResolver();  
  }  
}

4. Ajoutons dans application.properties les donnees de la configuration
>keycloak.realm=app-realm  
keycloak.auth-server-url=http://localhost:8080/auth/  
keycloak.resource=NomdevotreApp  
keycloak.public-client=false   
keycloak.bearer-only=true

5. Après demarrage du microservice, si on tente d’accéder a l'url localhost://contributions ,  on tombe  sur l'authentification imposer par spring security. Donc nous devons ajouter la classe de configuration spring security en l'adaptant a la sécurité de keycloak. C'est a dire on délègue l'authentification de spring security a keycloak. 
Pour cela on définit une classe qui extends de **KeycloakWebSecurityConfigurerAdapter** avec trois methodes.
- Une première methode qui donne la stratégie de gestion des sessions 
> @Override  
protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {  
   return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());  
}
- Une deuxième methode qui délègue a keycloak la gestion des utilisateurs et des rôles. 
> @Override  
protected void configure(AuthenticationManagerBuilder auth) throws Exception {  
   auth.authenticationProvider(keycloakAuthenticationProvider());  
}
- Une troisième methode qui gère les autorisations   
> @Override  
protected void configure(HttpSecurity http) throws Exception {  
   super.configure(http);  
  http.authorizeRequests().antMatchers("/contributions/**").hasAnyAuthority("app-manager");  
}

6. Après redémarrage du micro-service, si on accède a l'url [http://localhost:8081/contributions]("") on remarque que la page d'authentification de spring Security n'est plus visible et nous avons une erreur status 401 Unauthorized ce qui montre que nous n'avons pas l'autorisation d’accéder a cette ressource. 

Notre micro-service attend un token valide pour authentifier le client avanr de lui permettre d’accéder a ce ressource.    




package com.atnb.africathenextbrain;

import com.atnb.africathenextbrain.dao.ContributionRepository;
import com.atnb.africathenextbrain.entities.Contribution;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.util.Date;

@SpringBootApplication
public class AfricathenextbrainApplication implements CommandLineRunner {
	@Autowired
	private ContributionRepository contributionRepository;
	@Autowired
	private RepositoryRestConfiguration restConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(AfricathenextbrainApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        restConfiguration.exposeIdsFor(Contribution.class);
        contributionRepository.save(new Contribution(null,"Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident sunt in culpa qui officia deserunt mollit anim id est laborum","El ibrahima DIAGO","Agriculture", "el-hadji-ibrahima-diago-11729466", new Date(),1));
		contributionRepository.save(new Contribution(null,"Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident sunt in culpa qui officia deserunt mollit anim id est laborum","Demba FALL","Science", "el-hadji-ibrahima-diago-11729466", new Date(),1));
        contributionRepository.save(new Contribution(null,"Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident sunt in culpa qui officia deserunt mollit anim id est laborum","Jean Noel SARR","Technologie", "el-hadji-ibrahima-diago-11729466", new Date(),1));
        contributionRepository.save(new Contribution(null,"Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident sunt in culpa qui officia deserunt mollit anim id est laborum","Abdou S DIOP","Transport", "el-hadji-ibrahima-diago-11729466", new Date(),1));
        contributionRepository.save(new Contribution(null,"Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident sunt in culpa qui officia deserunt mollit anim id est laborum","Mamadou Moustapha","Economie", "el-hadji-ibrahima-diago-11729466", new Date(),1));
        contributionRepository.save(new Contribution(null,"Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident sunt in culpa qui officia deserunt mollit anim id est laborum","Dr Ndeye A DIAGO","Peche", "el-hadji-ibrahima-diago-11729466", new Date(),1));

        contributionRepository.findAll().forEach(p->{
			System.out.println(p.toString());
		});
		System.out.println("salut africa");

	}
}

// class de configuration de keycloack
@Configuration
class keycloakConfig{
	@Bean
	KeycloakSpringBootConfigResolver configResolver(){
		return new KeycloakSpringBootConfigResolver();
	}
}

//class de configuration de spring security
//Ici On adapte la conf de spring securité a keyclock
@KeycloakConfiguration
class KeycloakSpringSecurityConfig extends KeycloakWebSecurityConfigurerAdapter{

	//Precision de la stratégie de gestion de la session
	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	/*  Ici on specifie comment spring security va chercher les utilisateur et les roles (BD etc..)
		Donc Ici nous dirons que cela cela gerer par keycloak
		keycloakAuthenticationProvider() est une methode qui retourne la config quil faut pour dire
		a spring secutité que nous deleguons la gestion des users et role a un provider keycloak
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(keycloakAuthenticationProvider());
	}


	/*
		Cette class gere les autorisations
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.authorizeRequests().antMatchers("/contributions/**").hasAnyAuthority("app-manager");
	}
}



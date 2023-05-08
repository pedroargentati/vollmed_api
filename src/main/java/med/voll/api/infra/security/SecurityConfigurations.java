package med.voll.api.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // -> indica ao spring que as configurações de segurança serão personalizadas.
public class SecurityConfigurations {

	@Bean // -> serve para expor o retorno do método (devolver para o spring).
	public SecurityFilterChain seurityFilterChain(HttpSecurity http) throws Exception {
		/**
		 * csrf -> disabilita a proteção contra ataques de Cross-Site Request Forgery;
		 * isso porque como será utilizado autenticação via token, ele mesmo já é uma
		 * proteção contra ataques.
		 */
		return http.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // muda a configuração para stateless ao invés de statefull.
				.and().build();
	}
	
	/**
	 * @description Ensina ao spring como injetar o objeto do tipo AuthenticationManager.
	 * 
	 * @param configuration
	 * @throws Exception
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	/**
	 * @description Ensina ao spring que deverá usar o algoritmo 'BCryptPasswordEncoder'.
	 */
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}

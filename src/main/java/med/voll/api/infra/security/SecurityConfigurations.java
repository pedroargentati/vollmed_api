package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // -> indica ao spring que as configurações de segurança serão personalizadas.
public class SecurityConfigurations {

	@Autowired
	private SecurityFilter securityFilter;
	
	@Bean // -> serve para expor o retorno do método (devolver para o spring).
	public SecurityFilterChain seurityFilterChain(HttpSecurity http) throws Exception {
		/**
		 * csrf -> disabilita a proteção contra ataques de Cross-Site Request Forgery;
		 * isso porque como será utilizado autenticação via token, ele mesmo já é uma
		 * proteção contra ataques.
		 */
		return http.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().authorizeHttpRequests()
				.requestMatchers(HttpMethod.POST, "/login").permitAll()
				.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
				.anyRequest().authenticated()
				.and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // adicioando o filtro da aplicação para rodar antes do filtro do spring.
				.build();
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

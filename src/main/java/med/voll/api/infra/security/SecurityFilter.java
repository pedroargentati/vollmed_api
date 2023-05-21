package med.voll.api.infra.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // -> classe/componente genérico da aplicação
public class SecurityFilter extends OncePerRequestFilter {

	/***
	 * Será disparado para todas as requisições (apenas uma vez por request) que passarem pela API.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		var tokenJWT = extrairToken(request);
		
		
		filterChain.doFilter(request, response);
	}

	private String extrairToken(HttpServletRequest request) {
		var authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null) {
			throw new RuntimeException("[ERRO] Token JWT não enviado no cabeçalho (Authorization) da requisição.");
		}
		return authorizationHeader.replace("Bearer ", "");
	}

}

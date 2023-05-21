package med.voll.api.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.infra.exception.SecurityException;

@Component // -> classe/componente genérico da aplicação
public class SecurityFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenService tokenService;

	/***
	 * Será disparado para todas as requisições (apenas uma vez por request) que passarem pela API.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		var tokenJWT = extrairToken(request);
		
		var subject = tokenService.getSubject(tokenJWT);
		System.out.println(subject);
		
		filterChain.doFilter(request, response);
	}

	/**
	 * Extrai o token enviado no header Authorization da requisição.
	 * @param request
	 * @return token enviado na request.
	 */
	private String extrairToken(HttpServletRequest request) {
		var authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null) {
			throw new SecurityException("[ERRO] Token JWT não enviado no cabeçalho (Authorization) da requisição.");
		}
		return authorizationHeader.replace("Bearer ", "");
	}

}

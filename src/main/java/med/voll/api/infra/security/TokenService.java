package med.voll.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import med.voll.api.domain.usuario.Usuario;
import med.voll.api.infra.exception.SecurityException;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;

	/**
	 * Utilizando a biblioteca JWT, gera o token para a aplicação.
	 *
	 * @param Usuario dados do usuário.
	 * @return token
	 */
	public String gerarToken(Usuario usuario) {
		try {
			var algoritmo = Algorithm.HMAC256(secret);
			return JWT.create()
					.withIssuer("API Voll.med")
					.withSubject(usuario.getLogin()) // usuário relacionado com este token.
					.withExpiresAt(dataExpiracao()) // validade do token.
					.sign(algoritmo);
		} catch (JWTCreationException exception) {
			throw new RuntimeException("[ERRO] Erro ao gerar o Token JWT.", exception);
		}
	}
	
	/**
	 * Verifica se o token passado como parâmetro é válido e retorna o usuário.
	 * @param tokenJWT
	 * @return usuário do token enviado.
	 * @throws SecurityException
	 */
	public String getSubject(String tokenJWT) {
		try {
			var algoritmo = Algorithm.HMAC256(secret);
		    return JWT.require(algoritmo)
		        .withIssuer("API Voll.med")
		        .build()
		        .verify(tokenJWT)
		        .getSubject(); // verifica se o token é válido
		} catch (JWTVerificationException exception){
		    throw new SecurityException("[ERRO] Token JWT inválido ou expirado.");
		}
	}

	/**
	 * Calcula a data de expiração do token adicionando 2 horas
	 * ao horário atual do sistema, considerando o fuso horário GMT-3.
	 *
	 * @return Objeto Instant que representa a data e hora de expiração.
	 */
	private Instant dataExpiracao() {
		return LocalDateTime.now()
				.plusHours(2)
				.toInstant(ZoneOffset.of("-03:00"));
	}
}

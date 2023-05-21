package med.voll.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import med.voll.api.domain.usuario.Usuario;

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

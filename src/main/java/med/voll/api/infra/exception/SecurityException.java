package med.voll.api.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // Defina o status HTTP adequado para a exceção
public class SecurityException extends RuntimeException {
	public SecurityException(String message) {
        super(message);
    }
}

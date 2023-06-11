package med.voll.api.infra.exception;

import java.nio.file.AccessDeniedException;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice // -> anotação do spring que indica que é uma calsse de tratamento de erros.
public class ErrorsHandler {

	/**
	 * @desctiption Se em qualquer controller do projeto for lançada uma Exception 'EntityNotFoundException',
	 * o método 'handleError404' será chamado.
	 * 
	 * @return Not Found exception (404).
	 **/
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> handleError404() {
		return ResponseEntity.notFound().build();
	}
	
	/**
	 * @desctiption Se em qualquer controller do projeto for lançada uma Exception 'MethodArgumentNotValidException',
	 * o método 'handleError400' será chamado.
	 * 
	 * @return Not Found exception (400).
	 **/
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleError400(MethodArgumentNotValidException exception) {
		var errors = exception.getFieldErrors();
		
		return ResponseEntity.badRequest().body(errors.stream().map(ValidationErrorData::new).toList());
	}
	
	/**
	 * @desctiption Se em qualquer controller do projeto for lançada uma Exception 'SecurityException',
	 * o método 'handleSecurityException' será chamado.
	 * 
	 * @return UNAUTHORIZED (401).
	 **/
	@ExceptionHandler(SecurityException.class)
	public ResponseEntity<String> handleSecurityException(SecurityException exception) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
	}


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> tratarErro400(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> tratarErroBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> tratarErroAcessoNegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> tratarErro500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " +ex.getLocalizedMessage());
    }

    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
	
	private record ValidationErrorData(String code, String message) {
		public ValidationErrorData(FieldError error) {
			this(error.getField(), error.getDefaultMessage());
		}
	}
	
}

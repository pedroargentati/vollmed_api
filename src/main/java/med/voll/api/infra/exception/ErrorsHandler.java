package med.voll.api.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	
	/**
	 * Método genérico para lidar com exceções não mapeadas
	 *
	 * @return INTERNAL SERVER ERROR (500)
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
	}
	
	private record ValidationErrorData(String code, String message) {
		public ValidationErrorData(FieldError error) {
			this(error.getField(), error.getDefaultMessage());
		}
	}
	
}

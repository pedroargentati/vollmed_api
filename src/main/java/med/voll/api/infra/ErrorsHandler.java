package med.voll.api.infra;

import org.springframework.http.ResponseEntity;
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
	
}

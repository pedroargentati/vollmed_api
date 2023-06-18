package med.voll.api.domain;

public class BusinessException extends RuntimeException {

	public BusinessException(String mensagem) {
		super(mensagem);
	}
	
	public BusinessException(String... mensagens) {
		 super(String.join("\n", mensagens));
	}
	
	public BusinessException() {
		super();
	}
	
}

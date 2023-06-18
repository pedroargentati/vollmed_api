package med.voll.api.domain.consulta.validacoes.cancelamento;

import med.voll.api.domain.BusinessException;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.MotivoCancelamento;

public class ValidadorMotivoCancelamentoConsulta implements ValidadorCancelamentoDeConsulta {

	@Override
	public void validar(DadosCancelamentoConsulta dados) {
		if (dados.motivoCancelamento() == null) {
			throw new BusinessException("É obrigatório informar o motivo de cancelamento.");
		}
		
		if (!verificarMotivoCancelamento(dados.motivoCancelamento())) {
			throw new BusinessException("Motivo de cancelamento informado é inválido.");
		}
		
	}

	public boolean verificarMotivoCancelamento(MotivoCancelamento motivo) {
	    for (MotivoCancelamento motivoEnum : MotivoCancelamento.values()) {
	        if (motivoEnum == motivo) {
	            return true; // O motivo de cancelamento existe no enum
	        }
	    }
	    return false; // O motivo de cancelamento não existe no enum
	}


}

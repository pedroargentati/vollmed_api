package med.voll.api.domain.consulta.validacoes;

import java.time.Duration;
import java.time.LocalDateTime;

import med.voll.api.domain.BusinessException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

public class ValidadorHorarioAntecedencia {

	public void validar(DadosAgendamentoConsulta dados) {
		var dataConsulta = dados.data();
		var horarioAtual = LocalDateTime.now();
		
		var diffEmMinutos = Duration.between(horarioAtual, dataConsulta).toMinutes();
		
		if (diffEmMinutos < 30) {
			throw new BusinessException("Consulta deve ser agendada com antecedencia mínima de 30 minutos.");
		}
		
	}
	
}

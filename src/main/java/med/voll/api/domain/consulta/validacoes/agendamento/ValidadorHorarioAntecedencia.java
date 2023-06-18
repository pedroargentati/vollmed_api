package med.voll.api.domain.consulta.validacoes.agendamento;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import med.voll.api.domain.BusinessException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoDeConsulta {

	public void validar(DadosAgendamentoConsulta dados) {
		var dataConsulta = dados.data();
		var horarioAtual = LocalDateTime.now();
		
		var diffEmMinutos = Duration.between(horarioAtual, dataConsulta).toMinutes();
		
		if (diffEmMinutos < 30) {
			throw new BusinessException("Consulta deve ser agendada com antecedencia mínima de 30 minutos.");
		}
		
	}
	
}

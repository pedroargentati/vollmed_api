package med.voll.api.domain.consulta.validacoes;

import java.time.LocalDateTime;

import med.voll.api.domain.BusinessException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

public class ValidadorPacienteSemOutraConsultaNoDia {

	private ConsultaRepository repository;
	
	public void validar(DadosAgendamentoConsulta dados) {
		
		LocalDateTime primeiroHorario = dados.data().withHour(7);
		LocalDateTime ultimoHorario = dados.data().withHour(18);
		Boolean pacientePossuiOutraConsultaNoDia = repository.existsByPacienteIdAndDataBetween(dados.idPaciente(), primeiroHorario, ultimoHorario);
		
		if (pacientePossuiOutraConsultaNoDia) {
			throw new BusinessException("Paciente já possui uma consulta agendada nesse dia!");
		}
		
	}
	
}

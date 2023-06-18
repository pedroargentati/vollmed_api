package med.voll.api.domain.consulta.validacoes.agendamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.domain.BusinessException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.paciente.PacienteRepository;

@Component
public class ValidadorPacienteAtivo implements ValidadorAgendamentoDeConsulta {

	@Autowired
	private PacienteRepository pacienteRepository;
	
	public void validar(DadosAgendamentoConsulta dados) {
		if (dados.idMedico() == null) return;
		
		var isPacienteAtivo = pacienteRepository.findAtivoById(dados.idMedico());
		if (!isPacienteAtivo)
			throw new BusinessException("Consulta não pode ser agendada com paciente excluído.");
		
	}
	
}

package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.BusinessException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.paciente.PacienteRepository;

public class ValidadorPacienteAtivo {

	private PacienteRepository pacienteRepository;
	
	public void validar(DadosAgendamentoConsulta dados) {
		if (dados.idMedico() == null) return;
		
		var isPacienteAtivo = pacienteRepository.findAtivoById(dados.idMedico());
		if (!isPacienteAtivo)
			throw new BusinessException("Consulta não pode ser agendada com paciente excluído.");
		
	}
	
}

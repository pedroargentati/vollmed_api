package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.BusinessException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

public class ValidadorMedicoComOutraConsultaNoMesmoHorario {

	private ConsultaRepository repository;
	
	public void validar(DadosAgendamentoConsulta dados) {
		
		Boolean medicoPossuiConsultaNoMesmoHorario = repository.existsByMedicoIdAndData(dados.idMedico(), dados.data());
		if (medicoPossuiConsultaNoMesmoHorario) {
			throw new BusinessException("Consulta não pode ser agendada porque o médico já tem outra consulta no mesmo horário!");
		}
	}
}

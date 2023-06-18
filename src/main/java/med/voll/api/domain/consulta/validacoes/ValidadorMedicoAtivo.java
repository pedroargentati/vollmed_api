package med.voll.api.domain.consulta.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.domain.BusinessException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.MedicoRepository;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoDeConsulta {

	@Autowired
	private MedicoRepository medicoRepository;
	
	public void validar(DadosAgendamentoConsulta dados) {
		if (dados.idMedico() == null) return;
		
		var isMedicoAtivo = medicoRepository.findAtivoById(dados.idMedico());
		if (!isMedicoAtivo)
			throw new BusinessException("Consulta não pode ser agendada com médico excluído.");
		
	}
	
}

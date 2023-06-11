package med.voll.api.domain.consulta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.BusinessException;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;

@Service
public class AgendaDeConsultas {

	@Autowired
	private ConsultaRepository consultaRepository;
	
	@Autowired
	private MedicoRepository medicoRepository;
	
	@Autowired
	private PacienteRepository pacienteRepository;
	
	public void agendar(DadosAgendamentoConsulta dados) {
		if (!pacienteRepository.existsById(dados.idPaciente())) {
			throw new BusinessException("Id do paciente informado não existe.");
		}
		
		if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
			throw new BusinessException("Id do médico informado não existe.");			
		}
		
		var medico = escolherMedico(dados);
		var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
		var consulta = new Consulta(null, medico, paciente, dados.data());
		
		consultaRepository.save(consulta);
	}

	private Medico escolherMedico(DadosAgendamentoConsulta dados) {
		if (dados.idMedico() != null) {
			return medicoRepository.getReferenceById(dados.idMedico());
		}
		
		if (dados.especialidade() == null) {
			throw new BusinessException("A especialidade é obrigatório quando médico não for escolhido!");
		}
		
		return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
		
	}
	
	
	
}

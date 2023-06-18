package med.voll.api.domain.consulta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.BusinessException;
import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
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
	
	// injeta todos os código de validação (todas as classes de validação que implenentam essa interface) de uma vez.
	@Autowired
	private List<ValidadorAgendamentoDeConsulta> validadores;
	
	@Autowired
	private List<ValidadorCancelamentoDeConsulta> validadorCancelamento;
	
	public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {
		if (!pacienteRepository.existsById(dados.idPaciente())) {
			throw new BusinessException("Id do paciente informado não existe.");
		}
		
		if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
			throw new BusinessException("Id do médico informado não existe.");			
		}
		
		// percorre a lista de validadores e chama o método validar
		validadores.forEach(v -> v.validar(dados));
		
		var medico = escolherMedico(dados);
		
		if (medico == null) {
			throw new BusinessException("Não existe médico disponível nessa data.");
		}
		
		var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
		var consulta = new Consulta(null, medico, paciente, dados.data());
		
		consultaRepository.save(consulta);
		return new DadosDetalhamentoConsulta(consulta);
	}
	
	public DadosCancelamentoConsulta cancelar(DadosCancelamentoConsulta dados) {
		if (!consultaRepository.existsById(dados.idConsulta())) {
			throw new BusinessException("Id da consulta a ser cancelada não foi encontrada.");
		}
		
		validadorCancelamento.forEach(v -> v.validar(dados));
		
	    var consulta = consultaRepository.getReferenceById(dados.idConsulta());
	    consultaRepository.deleteById(dados.idConsulta());
		
		return null;
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

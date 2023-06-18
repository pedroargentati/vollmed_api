package med.voll.api.domain.consulta.validacoes.cancelamento;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.domain.BusinessException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;

@Component
public class ValidadorCancelamentoComAntecedencia implements ValidadorCancelamentoDeConsulta {

	@Autowired
	private ConsultaRepository consultaRepository;
	
	@Override
	public void validar(DadosCancelamentoConsulta dados) {
		LocalDateTime now = LocalDateTime.now();
		var consulta = consultaRepository.getReferenceById(dados.idConsulta());		
		var diffHorario = Duration.between(now, consulta.getData()).toHours();
		
		if (diffHorario < 24) {
			throw new BusinessException("A consulta só pode ser cancelada com mais de 24 horas de antecedência.");
		}
		
	}
	
}

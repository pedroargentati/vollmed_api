package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;

@Controller
@RequestMapping("medicos")
public class MedicoController {

	@Autowired // -> o spring que vai instanciar passar para o controller automaticamente (injeção de dependencias).
	private MedicoRepository medicoRepository;
	
	@PostMapping
	@ResponseBody
	@Transactional
	public void cadastrarMedico(@RequestBody @Valid DadosCadastroMedico dados) {
		/**
		 * No controller não recebemos um objeto do tipo médico, e sim um DTO (DadosCadastoMedico)
		 * então será necessário fazer uma conversão de DTO para um objeto do tipo Medico (JPA Entity).
		 */
		medicoRepository.save(new Medico(dados));
	}
}

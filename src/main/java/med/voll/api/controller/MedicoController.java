package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import med.voll.api.medico.DadosAlteracaoMedico;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosListagemMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;

@RestController
@RequestMapping("medicos")
public class MedicoController {

	@Autowired // -> o spring que vai instanciar passar para o controller automaticamente (injeção de dependencias).
	private MedicoRepository medicoRepository;
	
	@GetMapping
	public Page<DadosListagemMedico> obterListaMedicos(@PageableDefault(size = 15, sort = {"nome"}) Pageable paginacao /**Serve para realizar a paginação*/) {
		return medicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
	}
	
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
	
	@PutMapping
	@ResponseBody
	@Transactional
	public void atulizarMedico(@RequestBody @Valid DadosAlteracaoMedico dados) {
		var medico = medicoRepository.getReferenceById(dados.id());
		medico.atualizarInformacoes(dados);
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	@Transactional
	public void excluirMedico(@PathVariable("id") Long id) {
		var medico = medicoRepository.getReferenceById(id);
		medico.excluir();
	}

}

package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import med.voll.api.medico.DadosAlteracaoMedico;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosDetalhamentoMedio;
import med.voll.api.medico.DadosListagemMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;

@RestController
@RequestMapping("medicos")
public class MedicoController {

	@Autowired // -> o spring que vai instanciar passar para o controller automaticamente (injeção de dependencias).
	private MedicoRepository medicoRepository;
	
	@GetMapping
	public ResponseEntity<Page<DadosListagemMedico>> obterListaMedicos(@PageableDefault(size = 15, sort = {"nome"}) Pageable paginacao /**Serve para realizar a paginação*/) {
		var page =  medicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> obterMedicoPorId(@PathVariable("id") Long id) {
		var medico = medicoRepository.getReferenceById(id);
		return ResponseEntity.ok(new DadosDetalhamentoMedio(medico));
	}
	
	@PostMapping
	@ResponseBody
	@Transactional
	public ResponseEntity<?> cadastrarMedico(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
		/**
		 * No controller não recebemos um objeto do tipo médico, e sim um DTO (DadosCadastoMedico)
		 * então será necessário fazer uma conversão de DTO para um objeto do tipo Medico (JPA Entity).
		 */
		var medico = new Medico(dados);
		medicoRepository.save(medico);
		
		/** Precisamos devolver aonde o recurso foi criado. Para isso, usamos a classe do Spring  'UriComponentsBuilder'
		 *  e junto com ela o ID do médico que foi criado.
		 */
		var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

		                      // 201
		return ResponseEntity.created(uri).body(new DadosDetalhamentoMedio(medico));
	}
	
	@PutMapping
	@ResponseBody
	@Transactional
	public ResponseEntity<DadosDetalhamentoMedio> atulizarMedico(@RequestBody @Valid DadosAlteracaoMedico dados) {
		var medico = medicoRepository.getReferenceById(dados.id());
		medico.atualizarInformacoes(dados);
		return ResponseEntity.ok(new DadosDetalhamentoMedio(medico));
	}
	
	
	@DeleteMapping("/{id}")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> excluirMedico(@PathVariable("id") Long id) {
		var medico = medicoRepository.getReferenceById(id);
		medico.excluir();
		
		return ResponseEntity.noContent().build();
	}

}

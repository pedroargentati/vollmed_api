package med.voll.api.domain.medico;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureJsonTesters
class ConsultaControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJacksonTester;
	@Autowired
	private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJacksonTester;
	@MockBean
	private AgendaDeConsultas agendaDeConsultas;

	@Test
	@DisplayName("Deveria devolver o código http 400 quando as informações estiverem incorretas")
	@WithMockUser
	void agendarCenario1() throws Exception {
		var response = mockMvc.perform(post("/consultas")).andReturn().getResponse();
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	@DisplayName("Deveria devolver o código http 200 quando as informações estiverem válidas")
	@WithMockUser
	void agendarCenario2() throws Exception {
		var data = LocalDateTime.now().plusHours(2);
		var especialidade = Especialidade.CARDIOLOGIA;
		var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 21L, 51L, data);
		
		when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento);
		
		var response = mockMvc
				.perform(post("/consultas").contentType(MediaType.APPLICATION_JSON)
						.content(String.valueOf(dadosAgendamentoConsultaJacksonTester
								.write(new DadosAgendamentoConsulta(21L, 51L, data, especialidade)).getJson())))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
		var jsonEsperado = dadosDetalhamentoConsultaJacksonTester
				.write(new DadosDetalhamentoConsulta(null, 21L, 51L, data)).getJson();
		
		assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
}

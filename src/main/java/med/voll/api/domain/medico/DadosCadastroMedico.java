package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroMedico(
			@NotBlank(message = "O campo 'nome' não pode ser nulo/vazio.")
			String nome,
			
			@NotBlank(message = "O campo 'email' não pode ser nulo/vazio.")
			@Email
			String email,
			
			@NotBlank(message = "O campo 'telefone' não pode ser nulo/vazio.")
			String telefone,

			@NotBlank(message = "O campo 'crm' não pode ser nulo/vazio.")
			@Pattern(regexp = "\\d{4,6}", message = "Formato para o CRM está inválido!")
			String crm,
			
			@NotNull(message = "O campo 'especialidade' não pode ser nulo/vazio.")
			Especialidade especialidade, 
			
			@NotNull(message = "O campo 'endereco' não pode ser nulo/vazio.")
			@Valid
			DadosEndereco endereco ) {

}

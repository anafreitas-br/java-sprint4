package br.com.imrea.business;

import br.com.imrea.model.entities.Medico;
import br.com.imrea.model.entities.Paciente;
import br.com.imrea.model.entities.Pessoa;
import br.com.imrea.repository.PessoaRepository;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Regras de negócio relacionadas às operações de pessoas, médicos e pacientes.
 * Define as condições para cadastro, atualização e validação dos dados pessoais.
 */
public class PessoaBusiness {
    private final PessoaRepository pessoaRepo = new PessoaRepository();

    public PessoaBusiness() throws SQLException {}

    /**
     * Regra: uma pessoa só pode ser cadastrada se tiver nome, e-mail válido, sexo e telefone preenchidos.
     */
    public Pessoa cadastrarPessoa(Pessoa pessoa) throws SQLException {
        validarPessoa(pessoa);
        pessoaRepo.cadastrarPessoa(pessoa);
        return pessoa;
    }

    /**
     * Regra: uma pessoa só pode ser atualizada se continuar atendendo aos critérios de validação.
     */
    public void atualizarPessoa(Pessoa pessoa) throws SQLException {
        validarPessoa(pessoa);
        pessoaRepo.updatePessoa(pessoa);
    }

    /**
     * Regra: um médico só pode ser cadastrado se for uma pessoa válida e possuir CRM informado.
     */
    public void cadastrarMedico(Medico medico) throws SQLException {
        validarPessoa(medico.getPessoa());
        if (medico.getCrm() == null || medico.getCrm().isBlank()) {
            throw new IllegalArgumentException("O CRM do médico é obrigatório.");
        }
        pessoaRepo.saveMedico(medico);
    }

    /**
     * Regra: um paciente só pode ser cadastrado se for uma pessoa válida e possuir CNS informado.
     */
    public void cadastrarPaciente(Paciente paciente) throws SQLException {
        validarPessoa(paciente.getPessoa());
        if (paciente.getCns() == null || paciente.getCns().isBlank()) {
            throw new IllegalArgumentException("O CNS do paciente é obrigatório.");
        }
        pessoaRepo.savePaciente(paciente);
    }

    /**
     * Regra: permite buscar pessoas, médicos ou pacientes pelo identificador único (ID).
     */
    public Optional<Pessoa> buscarPessoaPorId(long id) throws SQLException {
        return pessoaRepo.findPessoaById(id);
    }

    public Optional<Medico> buscarMedicoPorId(long id) throws SQLException {
        return pessoaRepo.findMedicoById(id);
    }

    public Optional<Paciente> buscarPacientePorId(long id) throws SQLException {
        return pessoaRepo.findPacienteById(id);
    }

    /**
     * Regra: a pessoa precisa ter nome não vazio, e-mail com "@", sexo definido e número de telefone com pelo menos 8 dígitos.
     */
    private void validarPessoa(Pessoa p) {
        if (p.getNome() == null || p.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da pessoa é obrigatório.");
        }
        if (p.getEmail() == null || !p.getEmail().contains("@")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        if (p.getSexo() == null) {
            throw new IllegalArgumentException("Sexo é obrigatório.");
        }
        if (p.getTelNumero() == null || p.getTelNumero().length() < 8) {
            throw new IllegalArgumentException("Número de telefone inválido.");
        }
    }
}

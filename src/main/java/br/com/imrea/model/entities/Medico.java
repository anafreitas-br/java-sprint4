package br.com.imrea.model.entities;

import br.com.imrea.model.enums.Especialidade;

public class Medico {
    private long id;
    private String crm;
    private Especialidade especialidade;
    private Pessoa pessoa;

    public Medico() {
    }

    public Medico(long id, String crm, Especialidade especialidade, Pessoa pessoa) {
        this.id = id;
        this.crm = crm;
        this.especialidade = especialidade;
        this.pessoa = pessoa;
    }

    public Medico(long id, String nome, String crm, Especialidade especialidade) {
        this.id = id;
        this.crm = crm;
        this.especialidade = especialidade;
        this.pessoa = new Pessoa();
        this.pessoa.setId(id);
        this.pessoa.setNome(nome);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCrm() {
        return this.crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Especialidade getEspecialidade() {
        return this.especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}


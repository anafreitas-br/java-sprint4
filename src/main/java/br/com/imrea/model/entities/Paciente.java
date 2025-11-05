package br.com.imrea.model.entities;

public class Paciente {
    private long id;
    private String cns;
    private Pessoa pessoa;

    public Paciente() {
    }

    public Paciente(long id, String cns, Pessoa pessoa) {
        this.id = id;
        this.cns = cns;
        this.pessoa = pessoa;
    }

    public Paciente(long id, String nome, String cns) {
        Pessoa p = new Pessoa();
        p.setId(id);
        p.setNome(nome);
        this.pessoa = p;
        this.cns = cns;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCns() {
        return this.cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}

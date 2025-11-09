package br.com.imrea.util;


public class RelatorioSemanalTO {
    private String nome;
    private RelatorioEspecialidadeTO atual;
    private RelatorioEspecialidadeTO anterior;

    public RelatorioSemanalTO(String nome, RelatorioEspecialidadeTO atual, RelatorioEspecialidadeTO anterior) {
        this.nome = nome;
        this.atual = atual;
        this.anterior = anterior;
    }

    public RelatorioSemanalTO() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public RelatorioEspecialidadeTO getAtual() {
        return atual;
    }

    public void setAtual(RelatorioEspecialidadeTO atual) {
        this.atual = atual;
    }

    public RelatorioEspecialidadeTO getAnterior() {
        return anterior;
    }

    public void setAnterior(RelatorioEspecialidadeTO anterior) {
        this.anterior = anterior;
    }
}


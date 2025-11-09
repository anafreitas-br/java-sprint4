package br.com.imrea.util;

public class RelatorioEspecialidadeTO {
    private String nome;
    private int consultas;
    private int confirmadas;
    private int realizadas;
    private int canceladas;

    public RelatorioEspecialidadeTO(String nome, int consultas, int confirmadas, int realizadas, int canceladas) {
        this.nome = nome;
        this.consultas = consultas;
        this.confirmadas = confirmadas;
        this.realizadas = realizadas;
        this.canceladas = canceladas;
    }

    public RelatorioEspecialidadeTO() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getConsultas() {
        return consultas;
    }

    public void setConsultas(int consultas) {
        this.consultas = consultas;
    }

    public int getConfirmadas() {
        return confirmadas;
    }

    public void setConfirmadas(int confirmadas) {
        this.confirmadas = confirmadas;
    }

    public int getRealizadas() {
        return realizadas;
    }

    public void setRealizadas(int realizadas) {
        this.realizadas = realizadas;
    }

    public int getCanceladas() {
        return canceladas;
    }

    public void setCanceladas(int canceladas) {
        this.canceladas = canceladas;
    }
}

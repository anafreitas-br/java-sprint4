package br.com.imrea.util;

public class RelatorioPacienteTO {
    private String nome;
    private int total;
    private int realizadas;
    private int canceladas;
    private int naoCompareceu;


    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
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
    public int getNaoCompareceu() {
        return naoCompareceu;
    }
    public void setNaoCompareceu(int naoCompareceu) {
        this.naoCompareceu = naoCompareceu;
    }
}

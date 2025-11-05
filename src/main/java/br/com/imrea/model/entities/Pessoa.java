package br.com.imrea.model.entities;

import br.com.imrea.model.enums.Sexo;
import br.com.imrea.model.enums.TelTipo;

import java.time.LocalDate;

public class Pessoa {
    private long id;
    private String nome;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private String ddd;
    private String telNumero;
    private TelTipo telTipo;
    private String email;
    private String logradouro;
    private String numeroEndereco;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    public Pessoa() {
    }

    public Pessoa(long id, String nome, LocalDate dataNascimento, Sexo sexo, String ddd, String telNumero, TelTipo telTipo, String email, String logradouro, String numeroEndereco, String complemento, String bairro, String cidade, String estado, String cep) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.ddd = ddd;
        this.telNumero = telNumero;
        this.telTipo = telTipo;
        this.email = email;
        this.logradouro = logradouro;
        this.numeroEndereco = numeroEndereco;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public Pessoa(long id, String nome, String telNumero, String email) {
        this.id = id;
        this.nome = nome;
        this.telNumero = telNumero;
        this.email = email;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Sexo getSexo() {
        return this.sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getDdd() {
        return this.ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getTelNumero() {
        return this.telNumero;
    }

    public void setTelNumero(String telNumero) {
        this.telNumero = telNumero;
    }

    public TelTipo getTelTipo() {
        return this.telTipo;
    }

    public void setTelTipo(TelTipo telTipo) {
        this.telTipo = telTipo;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogradouro() {
        return this.logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumeroEndereco() {
        return this.numeroEndereco;
    }

    public void setNumeroEndereco(String numero) {
        this.numeroEndereco = numero;
    }

    public String getComplemento() {
        return this.complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return this.bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return this.cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return this.cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}

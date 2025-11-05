package br.com.imrea.model.entities;

import br.com.imrea.model.enums.PerfilUsuario;

public class Usuario {
    private long id;
    private String nomeUser;
    private String login;
    private String senha;
    private PerfilUsuario perfil;

    public Usuario() {
    }

    public Usuario(long id, String nomeUser, String login, String senha, PerfilUsuario perfil) {
        this.id = id;
        this.nomeUser = nomeUser;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeUser() {
        return this.nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public PerfilUsuario getPerfil() {
        return this.perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }
}

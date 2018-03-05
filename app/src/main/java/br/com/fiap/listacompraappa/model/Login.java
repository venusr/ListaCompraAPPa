package br.com.fiap.listacompraappa.model;

/**
 * Created by Cecilia_2 on 11/02/2018.
 */

import com.google.gson.annotations.SerializedName;

public class Login {

    private String id;
    private String usuario;
    private String senha;
    private String nome;

    public Login(){

    }

    public Login(String id, String usuario, String senha, String Nome) {
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
        this.nome = nome;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

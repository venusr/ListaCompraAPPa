package br.com.fiap.listacompraappa.model;

/**
 * Created by Cecilia_2 on 11/02/2018.
 */

public class Produto {

    private String id;
    private String nome;
    private String qtde;

    private Login login;

    public Produto() {

    }

    public Produto(String id, String nome, String qtde, Login login) {
        this.id = id;
        this.nome = nome;
        this.qtde = qtde;
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQtde() {
        return qtde;
    }

    public void setQtde(String qtde) {
        this.qtde = qtde;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

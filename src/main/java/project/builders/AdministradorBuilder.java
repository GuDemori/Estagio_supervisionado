package project.builders;

import project.entities.Administrator;

public class AdministradorBuilder {

    private String nome;
    private String login;
    private String senha;
    private String documentoIdentificacao;

    public AdministradorBuilder setLogin(String login){
        this.login = login;
        return this;
    }
    public AdministradorBuilder setSenha(String senha){
        this.senha = senha;
        return this;
    }
    public AdministradorBuilder setDocumentoIdentificacao(String documentoIdentificacao){
        this.documentoIdentificacao = documentoIdentificacao;
        return this;
    }
    public AdministradorBuilder setNome(String nome){
        this.nome = nome;
        return this;
    }

    public Administrator build(){
        return new Administrator(nome, login, senha, documentoIdentificacao);
    }

}


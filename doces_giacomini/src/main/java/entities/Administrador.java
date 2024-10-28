package entities;

import builders.AdministradorBuilder;

public class Administrador extends Usuario {

    private String nome;
    public Administrador(String nome, String login, String senha, String documentoIdentificação) {
        super(login, senha, documentoIdentificação);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public static AdministradorBuilder builder(){
        return new AdministradorBuilder();
    }

}

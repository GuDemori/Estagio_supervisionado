package entities;

public abstract class Usuario {
    private String login;
    private String senha;
    private String documentoIdentificação;

    public Usuario(String login, String senha, String documentoIdentificação) {
        this.login = login;
        this.senha = senha;
        this.documentoIdentificação = documentoIdentificação;
    }
}
package projeto.entities;

import projeto.builders.AdministradorBuilder;
import jakarta.persistence.*;

@Entity
@Table(name = "administrador")
public class Administrador {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;
    @Column(name = "LOGIN", nullable = false)
    private String login;
    @Column(name = "SENHA", nullable = false)
    private String senha;
    @Column(name = "DOCUMENTO_IDENTIFICACAO")
    private String documentoIdentificacao;

    public Administrador(String nome, String login, String senha, String documentoIdentificacao) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.documentoIdentificacao = documentoIdentificacao;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setDocumentoIdentificacao(String documentoIdentificacao) {
        this.documentoIdentificacao = documentoIdentificacao;
    }

    public String getDocumentoIdentificacao() {
        return documentoIdentificacao;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public static AdministradorBuilder builder(){
        return new AdministradorBuilder();
    }

}

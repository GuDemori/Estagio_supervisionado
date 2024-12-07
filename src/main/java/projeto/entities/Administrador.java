package projeto.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "administrador")
public class Administrador extends Usuario {

    @NotBlank(message = "Nome não pode ser vazio")
    @Column(name = "NOME", nullable = false)
    private String nome;

    public Administrador(String nome, String login, String senha, String documentoIdentificacao) {
        super(login, senha, documentoIdentificacao);
        this.nome = nome;
    }

    public Administrador() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

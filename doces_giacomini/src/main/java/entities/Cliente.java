package entities;

import builders.ClienteBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nome_estabelecimento")
    private String nomeEstabelecimento;
    @Column(length = 255)
    private String cidade;
    @NotNull
    @Column(length = 255)
    private String endereco;


    public Cliente(String login, String senha, String documentoIdentificação) {
        super(login, senha, documentoIdentificação);
        this.nomeEstabelecimento = nomeEstabelecimento;
        this.cidade = cidade;
        this.endereco = endereco;
    }

    public String getCidade() { return cidade; }

    public String getEndereco() { return endereco; }

    public String getNomeEstabelecimento(){
        return nomeEstabelecimento;
    }

    public static ClienteBuilder builder(){
        return new ClienteBuilder();
    }

}

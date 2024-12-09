package project.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "fornecedor")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NOME_FORNECEDOR")
    private String nomeFornecedor;
    @Column(name = "TELEFONE_CONTATO_FORNECEDOR")
    private String telefoneContatoFornecedor;
    @Column(name = "ENDERECO_FORNECEDOR")
    private String enderecoFornecedor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    public String getTelefoneContatoFornecedor() {
        return telefoneContatoFornecedor;
    }

    public void setTelefoneContatoFornecedor(String telefoneContatoFornecedor) {
        this.telefoneContatoFornecedor = telefoneContatoFornecedor;
    }

    public String getEnderecoFornecedor() {
        return enderecoFornecedor;
    }

    public void setEnderecoFornecedor(String enderecoFornecedor) {
        this.enderecoFornecedor = enderecoFornecedor;
    }

    public String getNomeFornecedor(){
        return nomeFornecedor;
    }

}

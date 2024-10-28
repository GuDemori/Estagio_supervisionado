package entities;

public class Fornecedor {

    private String nomeFornecedor;
    private String telefoneContatoFornecedor;
    private String enderecoFornecedor;

    public Fornecedor(String nomeFornecedor, String telefoneContatoFornecedor, String enderecoFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
        this.telefoneContatoFornecedor = telefoneContatoFornecedor;
        this.enderecoFornecedor = enderecoFornecedor;
    }

    public String getNomeFornecedor(){
        return nomeFornecedor;
    }

}

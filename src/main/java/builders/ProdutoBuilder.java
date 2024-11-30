package builders;

import entities.Produto;

public class ProdutoBuilder {

    private String nomeProduto;
    private String descricao;
    private double preco;
    private int quantidade;
    private String sabor = "";
    private String nomeFornecedor;

    public ProdutoBuilder setNomeProduto(String nomeProduto){
        this.nomeProduto = nomeProduto;
        return this;
    }
    public ProdutoBuilder setDescricao(String descricao){
        this.descricao = descricao;
        return this;
    }

    public ProdutoBuilder setPreco(double preco){
        if(preco <= 0){
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
        this.preco = preco;
        return this;
    }

    public ProdutoBuilder setQuantidade(int quantidade){
        if(quantidade < 0){
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.quantidade = quantidade;
        return this;
    }

    public ProdutoBuilder setSabor(String sabor){
        this.sabor = sabor;
        return this;
    }

    public ProdutoBuilder setNomeFornecedor(String nomeFornecedor){
        this.nomeFornecedor = nomeFornecedor;
        return this;
    }
    public Produto build(){
        if(nomeProduto.isEmpty()){
            throw new IllegalStateException("O produto deve ter um nome");
        }
        if(descricao.isEmpty()){
            throw new IllegalStateException("A descrição do produto é obrigatória");
        }
        if (preco <= 0) {
            throw new IllegalStateException("Preço não pode ser menor do que Zero");
        }
        if(quantidade < 0){
            throw new IllegalStateException("Quantidade não pode ser inferior a 0");
        }
        return new Produto(nomeProduto, preco, descricao, quantidade, sabor, nomeFornecedor);
    }
}

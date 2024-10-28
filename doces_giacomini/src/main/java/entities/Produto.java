package entities;

import builders.ProdutoBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.mapping.Set;

@Entity
@Table(name = "produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nome_produto", nullable = false)
    private String nomeProduto;
    @Column(nullable = false)
    private double preco;
    @Column(length = 255)
    private String descricao;
    @NotNull
    @Min(0)
    private int quantidade;
    @Column(length = 50)
    private String sabor;
    @Column(length = 255)
    private String nomeFornecedor;

    public Produto(String nomeProduto, double preco, String descricao, int quantidade, String sabor, String nomeFornecedor) {
        this.nomeProduto = nomeProduto;
        this.preco = preco;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.sabor = sabor;
        this.nomeFornecedor = nomeFornecedor;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public void setNomeFornecedor(String nomeFornecedor) { this.nomeFornecedor = nomeFornecedor; }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getId() { return id; }
    public String getNomeProduto() {
        return nomeProduto;
    }

    public double getPreco() {
        return preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getSabor() {
        return sabor;
    }

    public String getNomeFornecedor(){
        return nomeFornecedor;
    }

    public Produto reduzirQuantidade(int quantidadeVendida) {

        if (quantidadeVendida > this.quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente para " + getNomeProduto());
        }
        return new Produto(nomeProduto, preco, descricao, this.quantidade - quantidadeVendida, sabor, nomeFornecedor);
    }

    public static ProdutoBuilder builder(){
        return new ProdutoBuilder();
    }

}

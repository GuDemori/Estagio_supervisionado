package entities;

import builders.ProdutoBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "PRODUTO")
public class Produto {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(Produto.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUTO_ID")
    private Long id;

    @Column(name = "NOME_PRODUTO", nullable = false, length = 100)
    private String nomeProduto;

    @Column(name = "PRECO",nullable = false)
    @Min(0)
    private double preco;

    @Column(name = "DESCRICAO",length = 255)
    private String descricao;

    @NotNull
    @Min(0)
    @Column(name = "QUANTIDADE")
    private int quantidade;

    @Column(name = "SABOR",length = 50)
    private String sabor;

    @Column(name = "NOME_FORNECEDOR",length = 255)
    private String nomeFornecedor;

    public Produto(String nomeProduto, double preco, String descricao, int quantidade, String sabor, String nomeFornecedor) {
        this.nomeProduto = nomeProduto;
        this.preco = preco;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.sabor = sabor;
        this.nomeFornecedor = nomeFornecedor;
        logger.info("Produto criado: {}", nomeProduto);
    }

    public void setId(Long id) {
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
        if (quantidade < 0){
            logger.error("Tentativa de criar produto {} com quantidade inferior a zero", nomeProduto);
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.quantidade = quantidade;
    }

    public Long getId() { return id; }
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
            logger.error("Estoque insuficiente para o produto: {}. Qauntidade em estoque: {}. Tentativa de redução: {},",
                   nomeProduto, quantidade, quantidadeVendida);
            throw new IllegalArgumentException("Estoque insuficiente para " + getNomeProduto());
        }

        int novaQuantidade = this.quantidade - quantidadeVendida;
        setQuantidade(novaQuantidade);
        logger.info("Quantidade reduzida para o produto: {}. Nova quantidade em estoque: {}", quantidadeVendida, novaQuantidade);
        return this;
    }

    public static ProdutoBuilder builder(){
        return new ProdutoBuilder();
    }

}

package projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "PRODUTO_ESTOQUE")
public class ProdutoEstoque {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoEstoque.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUTO_ID", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "ESTOQUE_ID", nullable = false)
    private Estoque estoque;

    @NotNull
    @Min(0)
    @Column(name = "QUANTIDADE", nullable = false)
    private int quantidade;

    public ProdutoEstoque(){}

    public ProdutoEstoque(Produto produto, Estoque estoque, int quantidade){
        if (quantidade < 0){
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.produto = produto;
        this.estoque = estoque;
        this.quantidade = quantidade;
        logger.info("Produto {} criado no estoque {} com quantidade {}",
                produto.getNomeProduto(), estoque.getId(), quantidade);
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {

            logger.error("Quantidade não pode ser negativa para produtos em estoque. Produto: {}. Estoque: {}",
                    produto.getNomeProduto(), estoque.getId());
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.quantidade = quantidade;
        logger.info("Quantidade atualizada para o produto {} no estoque {}. Nova quantidade: {}",
                produto.getNomeProduto(), estoque.getId(), quantidade);
    }
}

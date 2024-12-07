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

    private static final String ERRO_QUANTIDADE_NEGATIVA = "Quantidade não pode ser negativa.";
    private static final String ERRO_QUANTIDADE_LOG = "Quantidade não pode ser negativa para produtos em estoque. Produto: {}. Estoque: {}";

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

    /**
     * Construtor padrão para JPA
     */
    public ProdutoEstoque(){}

    /**
     * Construtor completo para inicializar ProdutoEstoque
     *
     * @param produto
     * @param estoque
     * @param quantidade
     * @throws IllegalArgumentException se quantidade for negativa
     */
    public ProdutoEstoque(Produto produto, Estoque estoque, int quantidade){
        validarQuantidade(quantidade);
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

    /**
     *Valida se a quantidade é positiva
     *
     * @param quantidade
     * @throws IllegalArgumentException se quantidade for menor que 0
     */
    private void validarQuantidade(int quantidade){
        if (quantidade < 0){
            logError();
            throw new IllegalArgumentException(ERRO_QUANTIDADE_NEGATIVA);
        }
    }

    /**
     * Método utilizado para log de informações
     *
     * @param mensagem Mensagem para o log
     */
    private void logInfo(String mensagem){
        logger.info("{} Produto: {}. Estoque: {}. Quantidade: {}.",
                mensagem, produto != null ? produto.getNomeProduto() : "Desconhecido",
                estoque != null ? estoque.getId() : "Desconhecido", quantidade);
    }

    /**
     * Método utilizado para log de erros
     */
    private void logError(){
        logger.error(ERRO_QUANTIDADE_LOG,
                produto != null ? produto.getNomeProduto() : "Desconhecido",
                estoque != null ? estoque.getId() : "Desconhecido", quantidade);
    }

    /**
     * Representação em texto da entity ProdutoEstoque
     *
     * @return String com informações do ProdutoEstoque
     */
    @Override
    public String toString(){
        return String.format("ProdutoEstoque[id=%d, produto=%s, estoque=%s, quantidade=%d]",
                id, produto != null ? produto.getNomeProduto() : "Desconhecido",
                estoque != null ? estoque.getId() : "Desconhecido", quantidade);
    }

    public void setQuantidade(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidade = quantidade;
        logInfo("Quantidade atualizada para o produto no estoque.");
    }
}

package projeto.entities;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Entity
@Table(name = "ESTOQUE")
public class Estoque {

    private static final Logger logger = LoggerFactory.getLogger(Estoque.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESTOQUE_ID")
    private Long id;

    @OneToMany(mappedBy = "estoque", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProdutoEstoque> produtos;

    public Estoque(){}

    /**
     * Adiciona um novo produto no estoque
     *
     * @param produto
     * @param quantidade
     */
    public void adicionarProduto(Produto produto, int quantidade){
        validarProdutoEQuantidade(produto, quantidade);

        ProdutoEstoque produtoEstoque = new ProdutoEstoque(produto, this, quantidade);
        produtos.add(produtoEstoque);

        logger.info("produto {} adicionado ao estoque com quantidade {}",
                produto.getNomeProduto(), quantidade);
    }

    /**
     * Atualiza o estoque para um produto existente
     *
     * @param produto Produto a ser atualizado
     * @param quantidadeVendida Quantidade vendida
     */
    public void atualizarEstoque(Produto produto, int quantidadeVendida){

        validarProdutoEQuantidade(produto, quantidadeVendida);

        ProdutoEstoque produtoEstoque = buscarProdutoEstoque(produto);

        int novaQuantidade = produtoEstoque.getQuantidade() - quantidadeVendida;
        if (novaQuantidade < 0){
            String mensagemErro = String.format(
                    "Estoque insuficiente para o produto: %s. Quantidade atual: %d, Tentativa de venda: %d",
                    produto.getNomeProduto(), produtoEstoque.getQuantidade(), quantidadeVendida);
            logger.error(mensagemErro);
            throw new IllegalStateException(mensagemErro);
        }

        produtoEstoque.setQuantidade(novaQuantidade);
        logger.info("Estoque atualizado para o produto {}: nova quantidade: {}",
                produto.getNomeProduto(), novaQuantidade);

    }

    /**
     *  Exibe todos os produtos e suas quantidades no estoque.
     */
    public void exibirEstoque(){
        produtos.forEach(produtoEstoque ->
                logger.info("Produto: {}, Estoque: {}",
                        produtoEstoque.getProduto().getNomeProduto(),
                        produtoEstoque.getQuantidade()));
    }

    /**
     * Validação de produto e quantidade
     *
     * @param produto Produto a ser adicionado
     * @param quantidade Quantidade inicial do produto
     */
    private void validarProdutoEQuantidade(Produto produto, int quantidade) {

        if (produto == null || quantidade < 0){
            throw new IllegalArgumentException("Produto ou quantidade inválidos");
        }

    }

    /**
     * Busca um ProdutoEstoque pelo produto associado.
     *
     * @param produto Produto a ser buscado.
     * @return ProdutoEstoque associado ao produto.
     */
    private ProdutoEstoque buscarProdutoEstoque(Produto produto) {
        return produtos.stream()
                .filter(pe -> pe.getProduto().equals(produto))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Produto não encontrado no estoque: " + produto.getNomeProduto()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProdutoEstoque> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoEstoque> produtos) {
        this.produtos = produtos;
    }
}

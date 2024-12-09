package project.entities;

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
     * @param product
     * @param quantidade
     */
    public void adicionarProduto(Product product, int quantidade){
        validarProdutoEQuantidade(product, quantidade);

        ProdutoEstoque produtoEstoque = new ProdutoEstoque(product, this, quantidade);
        produtos.add(produtoEstoque);

        logger.info("produto {} adicionado ao estoque com quantidade {}",
                product.getProductName(), quantidade);
    }

    /**
     * Atualiza o estoque para um produto existente
     *
     * @param product Produto a ser atualizado
     * @param quantidadeVendida Quantidade vendida
     */
    public void atualizarEstoque(Product product, int quantidadeVendida){

        validarProdutoEQuantidade(product, quantidadeVendida);

        ProdutoEstoque produtoEstoque = buscarProdutoEstoque(product);

        int novaQuantidade = produtoEstoque.getQuantidade() - quantidadeVendida;
        if (novaQuantidade < 0){
            String mensagemErro = String.format(
                    "Estoque insuficiente para o produto: %s. Quantidade atual: %d, Tentativa de venda: %d",
                    product.getProductName(), produtoEstoque.getQuantidade(), quantidadeVendida);
            logger.error(mensagemErro);
            throw new IllegalStateException(mensagemErro);
        }

        produtoEstoque.setQuantidade(novaQuantidade);
        logger.info("Estoque atualizado para o produto {}: nova quantidade: {}",
                product.getProductName(), novaQuantidade);

    }

    /**
     *  Exibe todos os produtos e suas quantidades no estoque.
     */
    public void exibirEstoque(){
        produtos.forEach(produtoEstoque ->
                logger.info("Produto: {}, Estoque: {}",
                        produtoEstoque.getProduto().getProductName(),
                        produtoEstoque.getQuantidade()));
    }

    /**
     * Validação de produto e quantidade
     *
     * @param product Produto a ser adicionado
     * @param quantidade Quantidade inicial do produto
     */
    private void validarProdutoEQuantidade(Product product, int quantidade) {

        if (product == null || quantidade < 0){
            throw new IllegalArgumentException("Produto ou quantidade inválidos");
        }

    }

    /**
     * Busca um ProdutoEstoque pelo produto associado.
     *
     * @param product Produto a ser buscado.
     * @return ProdutoEstoque associado ao produto.
     */
    private ProdutoEstoque buscarProdutoEstoque(Product product) {
        return produtos.stream()
                .filter(pe -> pe.getProduto().equals(product))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Produto não encontrado no estoque: " + product.getProductName()));
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

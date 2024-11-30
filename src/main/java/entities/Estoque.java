package entities;

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

    public void adicionarProduto(Produto produto, int quantidade){
        try{
            ProdutoEstoque produtoEstoque = new ProdutoEstoque(produto, this, quantidade);
            produtos.add(produtoEstoque);
            logger.info("Produto {} adicionado ao estoque com quantidade: {}", produto.getNomeProduto(), quantidade);
        }catch(Exception e){
            logger.error("Erro ao adicionar produto {} ao estoque", produto.getNomeProduto());
            throw new RuntimeException("Erro ao adicionar produto ao estoque", e);
        }
    }

    public void atualizarEstoque(Produto produto, int quantidadeVendida){
        try{
            ProdutoEstoque produtoEstoque = produtos.stream()
                    .filter(pe -> pe.getProduto().equals(produto))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado no estoque"));

            int novaQuantidade = produtoEstoque.getQuantidade() - quantidadeVendida;
            if (novaQuantidade < 0){
                throw new IllegalStateException("Estoque insuficiente para o produto: " + produto.getNomeProduto());
            }

            produtoEstoque.setQuantidade(novaQuantidade);
            logger.info("Estoque atualizado para o produto {}: nova quantidade: {}", produto.getNomeProduto(), novaQuantidade);
        }catch (Exception e){
            logger.error("Erro ao atualizar o estoque para o produto {}", produto.getNomeProduto());
            throw new RuntimeException("Erro ao atualizar o estoque", e);
        }
    }

    public void exibirEstoque(){
        try{
            produtos.forEach(ProdutoEstoque -> logger.info("Produto: {}, Estoque: {}",
                    ProdutoEstoque.getProduto().getNomeProduto(),
                    ProdutoEstoque.getQuantidade()));
        }catch (Exception e){
            logger.error("Erro ao exibir o estoque", e);
            throw new RuntimeException("erro ao exibir o estoque", e);
        }
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

package projeto.controllers;

import projeto.entities.Produto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projeto.repository.ProdutoRepository;

import java.util.Optional;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private static final Logger logger = LoggerFactory.getLogger(EstoqueController.class);

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/{produtoId}")
    public Produto exibirInformacoesProduto(@PathVariable Long produtoId) {
        logger.info("Buscando informações do produto no estoque com ID: {}", produtoId);

        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> {
                    logger.error("Produto com ID {} não encontrado no estoque.", produtoId);
                    return new RuntimeException("Produto não encontrado no estoque.");
                });
    }

    @PutMapping("/atualizar/{produtoId}")
    public void atualizarEstoque(@PathVariable Long produtoId, @RequestParam int quantidade) {
        logger.info("Atualizando estoque do produto com ID: {}", produtoId);

        Optional<Produto> produtoOptional = produtoRepository.findById(produtoId);

        if (produtoOptional.isEmpty()) {
            logger.error("Produto com ID {} não encontrado para atualização.", produtoId);
            throw new RuntimeException("Produto não encontrado para atualização.");
        }

        Produto produto = produtoOptional.get();

        if (produto.getQuantidade() < quantidade) {
            logger.error("Estoque insuficiente para o produto com ID: {}. Quantidade disponível: {}", produtoId, produto.getQuantidade());
            throw new RuntimeException("Estoque insuficiente para o produto.");
        }

        produto.setQuantidade(produto.getQuantidade() - quantidade);

        try {
            produtoRepository.save(produto);
            logger.info("Estoque atualizado com sucesso para o produto: {}. Nova quantidade: {}", produto.getNomeProduto(), produto.getQuantidade());
        } catch (Exception e) {
            logger.error("Erro ao atualizar estoque do produto com ID: {}", produtoId, e);
            throw new RuntimeException("Erro ao atualizar o estoque.", e);
        }
    }

    @PutMapping("/adicionar/{produtoId}")
    public void adicionarNoEstoque(@PathVariable Long produtoId, @RequestParam int quantidade) {
        logger.info("Adicionando nova remessa ao estoque do produto com ID: {}", produtoId);

        Optional<Produto> produtoOptional = produtoRepository.findById(produtoId);

        if (produtoOptional.isEmpty()) {
            logger.error("Produto com ID {} não encontrado para adição ao estoque.", produtoId);
            throw new RuntimeException("Produto não encontrado para adição ao estoque.");
        }

        Produto produto = produtoOptional.get();

        produto.setQuantidade(produto.getQuantidade() + quantidade);

        try {
            produtoRepository.save(produto);
            logger.info("Nova remessa adicionada ao estoque para o produto: {}. Quantidade atualizada: {}", produto.getNomeProduto(), produto.getQuantidade());
        } catch (Exception e) {
            logger.error("Erro ao adicionar remessa ao estoque do produto com ID: {}", produtoId, e);
            throw new RuntimeException("Erro ao adicionar ao estoque.", e);
        }
    }

}

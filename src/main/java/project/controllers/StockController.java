package project.controllers;

import project.entities.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.repository.ProductRepository;

import java.util.Optional;

@RestController
@RequestMapping("/stock")
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private ProductRepository productRepository;

    /**
     * Busca informações de um produto no estoque.
     *
     * @param productId ID do produto.
     * @return Informações do produto.
     */
    @GetMapping("/{productId}")
    public Product getProductStockInfo(@PathVariable Long productId) {
        logger.info("Buscando informações do produto no estoque com ID: {}", productId);

        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.error("Produto com ID {} não encontrado no estoque.", productId);
                    return new RuntimeException("Produto não encontrado no estoque.");
                });
    }

    /**
     * Atualiza a quantidade de um produto no estoque.
     *
     * @param productId ID do produto.
     * @param quantity  Quantidade a ser reduzida.
     */
    @PutMapping("/update/{productId}")
    public void updateStock(@PathVariable Long productId, @RequestParam int quantity) {
        logger.info("Atualizando estoque do produto com ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.error("Produto com ID {} não encontrado.", productId);
                    return new RuntimeException("Produto não encontrado.");
                });

        if (product.getQuantity() < quantity) {
            logger.error("Estoque insuficiente para o produto com ID: {}.", productId);
            throw new RuntimeException("Estoque insuficiente.");
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        logger.info("Estoque atualizado para o produto {}. Nova quantidade: {}", product.getProductName(), product.getQuantity());
    }

    /**
     * Adiciona uma quantidade ao estoque de um produto.
     *
     * @param productId ID do produto.
     * @param quantity  Quantidade a ser adicionada.
     */
    @PutMapping("/add/{productId}")
    public void addToStock(@PathVariable Long productId, @RequestParam int quantity) {
        logger.info("Adicionando ao estoque do produto com ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.error("Produto com ID {} não encontrado.", productId);
                    return new RuntimeException("Produto não encontrado.");
                });

        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
        logger.info("Estoque atualizado para o produto {}. Nova quantidade: {}", product.getProductName(), product.getQuantity());
    }
}


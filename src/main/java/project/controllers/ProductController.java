package project.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.entities.Product;
import project.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            logger.error("Erro ao criar produto", e);
            throw new RuntimeException("Falha ao criar produto");
        }
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (!productRepository.existsById(id)) {
            logger.warn("Tentativa de atualizar produto inexistente com id {}", id);
            throw new RuntimeException("Produto não encontrado");
        }
        try {
            product.setId(id);
            logger.info("Produto atualizado com sucesso");
            return productRepository.save(product);
        } catch (Exception e) {
            logger.error("Erro ao atualizar produto", e);
            throw new RuntimeException("Não foi possível atualizar o produto");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            logger.error("Produto com id {} não encontrado", id);
            throw new RuntimeException("Não foi possível encontrar o produto com id " + id);
        }
        try {
            productRepository.deleteById(id);
            logger.info("Produto com id {} deletado", id);
        } catch (Exception e) {
            logger.error("Erro ao deletar produto", e);
            throw new RuntimeException("Erro ao deletar produto com id " + id);
        }
    }

    @GetMapping
    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String query) {
        logger.info("Buscando produtos com a query: {}", query);
        return productRepository.findAll().stream()
                .filter(product -> product.getProductName().toLowerCase().contains(query.toLowerCase()) ||
                        product.getNicknames().stream().anyMatch(nickname -> nickname.toLowerCase().contains(query.toLowerCase())))
                .collect(Collectors.toList());
    }
}

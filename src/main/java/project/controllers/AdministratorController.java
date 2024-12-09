package project.controllers;

import project.entities.Administrator;
import project.entities.Client;
import project.entities.Order;
import project.entities.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.repository.AdministratorRepository;
import project.repository.ClientRepository;
import project.repository.OrderRepository;
import project.repository.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdministratorController {

    private static final Logger logger = LoggerFactory.getLogger(AdministratorController.class);

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    // ==================== CLIENT METHODS ====================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/clients")
    public List<Client> listClients() {
        return clientRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/clients")
    public Client createClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/clients/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client updatedClient) {
        return clientRepository.findById(id)
                .map(client -> {
                    client.setEstablishmentName(updatedClient.getEstablishmentName());
                    client.setUsername(updatedClient.getUsername());
                    return ResponseEntity.ok(clientRepository.save(client));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(client -> {
                    clientRepository.delete(client);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ==================== ADMINISTRATOR METHODS ====================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Administrator createAdministrator(@RequestBody Administrator administrator) {
        try {
            logger.info("Administrator created with name {}", administrator.getName());
            return administratorRepository.save(administrator);
        } catch (Exception e) {
            logger.error("Error creating administrator", e);
            throw new RuntimeException("Failed to create administrator");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Administrator getAdministrator(@PathVariable Long id) {
        return administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Administrator updateAdministrator(@PathVariable Long id, @RequestBody Administrator administrator) {
        if (!administratorRepository.existsById(id)) {
            logger.error("Administrator not found");
            throw new RuntimeException("Administrator not found");
        }
        try {
            administrator.setId(id);
            logger.info("Administrator {} updated", administrator.getName());
            return administratorRepository.save(administrator);
        } catch (Exception e) {
            logger.error("Error updating administrator", e);
            throw new RuntimeException("Failed to update administrator");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteAdministrator(@PathVariable Long id) {
        if (!administratorRepository.existsById(id)) {
            logger.error("Administrator not found");
            throw new RuntimeException("Administrator not found");
        }
        try {
            administratorRepository.deleteById(id);
            logger.info("Administrator deleted");
        } catch (Exception e) {
            logger.error("Error deleting administrator", e);
            throw new RuntimeException("Failed to delete administrator");
        }
    }

    // ==================== PRODUCT METHODS ====================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/products")
    public List<Product> listProducts() {
        logger.info("Products listed");
        return productRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        logger.info("Product created");
        return productRepository.save(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setProductName(updatedProduct.getProductName());
                    //product.setSupplierName(updatedProduct.getSupplierName());
                    product.setPrice(updatedProduct.getPrice());
                    product.setDescription(updatedProduct.getDescription());
                    product.setFlavor(updatedProduct.getFlavor());
                    productRepository.save(product);
                    logger.info("Product updated");
                    return ResponseEntity.ok(product);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ==================== ORDER METHODS ====================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/orders/{clientId}")
    public Order createOrderForClient(@PathVariable Long clientId, @RequestBody Order order) {
        return clientRepository.findById(clientId)
                .map(client -> {
                    order.setClient(client);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }
}

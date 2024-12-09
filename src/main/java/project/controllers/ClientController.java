package project.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.entities.Client;
import project.entities.Order;
import project.entities.Product;
import project.repository.ClientRepository;
import project.repository.OrderRepository;
import project.repository.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/register")
    public ResponseEntity<Client> registerClient(@RequestBody Client client) {
        try {
            logger.info("Cliente registrado com sucesso com id {}", client.getId());
            return ResponseEntity.ok(clientRepository.save(client));
        } catch (Exception e) {
            logger.error("Erro ao registrar cliente", e);
            throw new RuntimeException("Falha ao registrar cliente");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(client -> {
                    logger.info("Detalhes do cliente acessados com sucesso para o cliente de id {}", id);
                    return ResponseEntity.ok(client);
                })
                .orElseThrow(() -> {
                    logger.error("Cliente com id {} não encontrado", id);
                    return new RuntimeException("Cliente não encontrado");
                });
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        if (!clientRepository.existsById(id)) {
            logger.error("Erro ao encontrar cliente com id {}", id);
            throw new RuntimeException("Cliente não encontrado");
        }
        try {
            client.setId(id);
            logger.info("Cliente com id {} atualizado com sucesso", id);
            return ResponseEntity.ok(clientRepository.save(client));
        } catch (Exception e) {
            logger.error("Erro ao atualizar cliente com id {}", id, e);
            throw new RuntimeException("Não foi possível atualizar o cliente");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (!clientRepository.existsById(id)) {
            logger.error("Erro ao encontrar cliente com id {} para exclusão", id);
            throw new RuntimeException("Cliente não encontrado");
        }
        try {
            clientRepository.deleteById(id);
            logger.info("Cliente com id {} deletado com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar cliente com id {}", id, e);
            throw new RuntimeException("Não foi possível deletar o cliente");
        }
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            logger.info("Produtos listados com sucesso");
            return ResponseEntity.ok(productRepository.findAll());
        } catch (Exception e) {
            logger.error("Erro ao listar produtos", e);
            throw new RuntimeException("Não foi possível listar os produtos");
        }
    }

    @PostMapping("/{clientId}/orders")
    @PreAuthorize("hasRole('ADMIN') or #clientId == authentication.principal.id")
    public ResponseEntity<Order> createOrder(@PathVariable Long clientId, @RequestBody Order order) {
        return clientRepository.findById(clientId)
                .map(client -> {
                    try {
                        order.setClient(client);
                        logger.info("Pedido criado com sucesso para o cliente com id {}", clientId);
                        return ResponseEntity.ok(orderRepository.save(order));
                    } catch (Exception e) {
                        logger.error("Erro ao criar pedido para o cliente com id {}", clientId, e);
                        throw new RuntimeException("Não foi possível criar o pedido");
                    }
                })
                .orElseThrow(() -> {
                    logger.error("Cliente com id {} não encontrado para criar pedido", clientId);
                    return new RuntimeException("Cliente não encontrado");
                });
    }
}

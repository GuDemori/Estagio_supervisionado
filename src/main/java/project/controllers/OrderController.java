package project.controllers;

import project.entities.Client;
import project.entities.Order;
import project.entities.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.entities.Stock;
import project.repository.ClientRepository;
import project.repository.StockRepository;
import project.repository.OrderRepository;
import project.repository.ProductRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    /**
     * Lista todos os pedidos de um cliente específico.
     *
     * @param clientId ID do cliente.
     * @return Lista de pedidos do cliente.
     */
    @GetMapping("/client/{clientId}")
    public List<Order> listClientOrders(@PathVariable Long clientId) {
        logger.info("Listando pedidos para o cliente com ID: {}", clientId);
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    logger.error("Cliente com ID {} não encontrado.", clientId);
                    return new RuntimeException("Cliente não encontrado.");
                });
        return orderRepository.findByClientId(client.getId());
    }

    /**
     * Cria um novo pedido para o cliente especificado.
     *
     * @param clientId ID do cliente.
     * @param newOrder Dados do novo pedido.
     * @return Pedido criado.
     */
    @PostMapping("/create/{clientId}")
    public Order createOrder(@PathVariable Long clientId, @RequestBody Order newOrder) {
        logger.info("Criando um novo pedido para o cliente com ID: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    logger.error("Cliente com ID {} não encontrado.", clientId);
                    return new RuntimeException("Cliente não encontrado.");
                });

        newOrder.setClient(client);
        newOrder.setOrderDate(LocalDate.now());
        try {
            return orderRepository.save(newOrder);
        } catch (Exception e) {
            logger.error("Erro ao criar pedido para o cliente com ID: {}", clientId, e);
            throw new RuntimeException("Erro ao criar pedido.", e);
        }
    }

    /**
     * Finaliza um pedido especificado.
     *
     * @param orderId ID do pedido.
     * @return Pedido finalizado.
     */
    @PutMapping("/finalize/{orderId}")
    public Order finalizeOrder(@PathVariable Long orderId) {
        logger.info("Finalizando pedido com ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Pedido com ID {} não encontrado.", orderId);
                    return new RuntimeException("Pedido não encontrado.");
                });

        Stock stock = stockRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado."));

        order.finalizeOrder(stock);
        order.setStatus("ENTREGUE");

        try {
            return orderRepository.save(order);
        } catch (Exception e) {
            logger.error("Erro ao finalizar pedido com ID: {}", orderId, e);
            throw new RuntimeException("Erro ao finalizar pedido.", e);
        }
    }

    /**
     * Copia um pedido existente.
     *
     * @param orderId ID do pedido a ser copiado.
     * @return Pedido copiado.
     */
    @PostMapping("/copy/{orderId}")
    public Order copyOrder(@PathVariable Long orderId) {
        logger.info("Copiando pedido com ID: {}", orderId);

        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Pedido com ID {} não encontrado.", orderId);
                    return new RuntimeException("Pedido não encontrado.");
                });

        Order newOrder = new Order();
        newOrder.setClient(existingOrder.getClient());
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setProducts(existingOrder.getProducts());

        try {
            return orderRepository.save(newOrder);
        } catch (Exception e) {
            logger.error("Erro ao copiar pedido com ID: {}", orderId, e);
            throw new RuntimeException("Erro ao copiar pedido.", e);
        }
    }
}


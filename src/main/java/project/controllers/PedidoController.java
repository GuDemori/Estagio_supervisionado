package project.controllers;

import project.entities.Client;
import project.entities.Estoque;
import project.entities.Order;
import project.entities.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.repository.ClientRepository;
import project.repository.EstoqueRepository;
import project.repository.OrderRepository;
import project.repository.ProductRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @GetMapping("/cliente/{clienteId}")
    public List<Order> listarPedidosCliente(@PathVariable Long clienteId) {
        logger.info("Listando pedidos para o cliente com ID: {}", clienteId);
        Optional<Client> cliente = clientRepository.findById(clienteId);
        if (cliente.isEmpty()) {
            logger.error("Cliente com ID {} não encontrado", clienteId);
            throw new RuntimeException("Cliente não encontrado");
        }
        return orderRepository.findByClienteId(cliente.get().getId());
    }

    @PostMapping("/criar/{clienteId}")
    public Order criarPedido(@PathVariable Long clienteId, @RequestBody Order novoOrder) {
        logger.info("Criando um novo pedido para o cliente com ID: {}", clienteId);

        Client client = clientRepository.findById(clienteId)
                .orElseThrow(() -> {
                    logger.error("Cliente com ID {} não encontrado", clienteId);
                    return new RuntimeException("Cliente não encontrado");
                });

        try {
            novoOrder.setClient(client);
            novoOrder.setOrderDate(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            for (Product product : novoOrder.getProducts()) {
                novoOrder.addProduct(product);
            }
            logger.info("Pedido criado com sucesso para o cliente: {}", client.getEstablishmentName());
            return orderRepository.save(novoOrder);
        } catch (Exception e) {
            logger.error("Erro ao criar pedido para o cliente com ID: {}", clienteId, e);
            throw new RuntimeException("Erro ao criar pedido", e);
        }
    }

    @PutMapping("/finalizar/{pedidoId}")
    public Order finalizarPedido(@PathVariable Long pedidoId) {
        logger.info("Finalizando pedido com ID: {}", pedidoId);

        Order order = orderRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    logger.error("Pedido com ID {} não encontrado", pedidoId);
                    return new RuntimeException("Pedido não encontrado");
                });

        try {
            Estoque estoque = estoqueRepository.findById(1L)
                            .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));
            order.finalizarPedido(estoque);
            order.setStatus("entregue");
            logger.info("Pedido finalizado com sucesso: {}", pedidoId);
            return orderRepository.save(order);
        } catch (Exception e) {
            logger.error("Erro ao finalizar o pedido com ID: {}", pedidoId, e);
            throw new RuntimeException("Erro ao finalizar pedido", e);
        }
    }

    @PostMapping("/copiar/{pedidoId}")
    public Order copiarPedido(@PathVariable Long pedidoId) {
        logger.info("Copiando pedido com ID: {}", pedidoId);

        Order orderExistente = orderRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    logger.error("Pedido com ID {} não encontrado", pedidoId);
                    return new RuntimeException("Pedido não encontrado");
                });

        try {
            Order novoOrder = new Order();
            novoOrder.setClient(orderExistente.getClient());
            novoOrder.setOrderDate(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            for (Product product : orderExistente.getProducts()) {
                novoOrder.addProduct(product);
            }
            logger.info("Pedido copiado com sucesso: {}", pedidoId);
            return orderRepository.save(novoOrder);
        } catch (Exception e) {
            logger.error("Erro ao copiar o pedido com ID: {}", pedidoId, e);
            throw new RuntimeException("Erro ao copiar pedido", e);
        }
    }
}

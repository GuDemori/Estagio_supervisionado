package projeto.controllers;

import projeto.entities.Cliente;
import projeto.entities.Estoque;
import projeto.entities.Pedido;
import projeto.entities.Produto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projeto.repository.ClienteRepository;
import projeto.repository.PedidoRepository;
import projeto.repository.ProdutoRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private Estoque estoque;

    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> listarPedidosCliente(@PathVariable Long clienteId) {
        logger.info("Listando pedidos para o cliente com ID: {}", clienteId);
        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        if (cliente.isEmpty()) {
            logger.error("Cliente com ID {} não encontrado", clienteId);
            throw new RuntimeException("Cliente não encontrado");
        }
        return pedidoRepository.findByClienteId(cliente.get().getId());
    }

    @PostMapping("/criar/{clienteId}")
    public Pedido criarPedido(@PathVariable Long clienteId, @RequestBody Pedido novoPedido) {
        logger.info("Criando um novo pedido para o cliente com ID: {}", clienteId);

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> {
                    logger.error("Cliente com ID {} não encontrado", clienteId);
                    return new RuntimeException("Cliente não encontrado");
                });

        try {
            novoPedido.setCliente(cliente);
            novoPedido.setDataPedido(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            for (Produto produto : novoPedido.getProdutos()) {
                novoPedido.adicionarProduto(produto);
            }
            logger.info("Pedido criado com sucesso para o cliente: {}", cliente.getNomeEstabelecimento());
            return pedidoRepository.save(novoPedido);
        } catch (Exception e) {
            logger.error("Erro ao criar pedido para o cliente com ID: {}", clienteId, e);
            throw new RuntimeException("Erro ao criar pedido", e);
        }
    }

    @PutMapping("/finalizar/{pedidoId}")
    public Pedido finalizarPedido(@PathVariable Long pedidoId) {
        logger.info("Finalizando pedido com ID: {}", pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    logger.error("Pedido com ID {} não encontrado", pedidoId);
                    return new RuntimeException("Pedido não encontrado");
                });

        try {
            pedido.finalizarPedido(estoque);
            pedido.setStatus("entregue");
            logger.info("Pedido finalizado com sucesso: {}", pedidoId);
            return pedidoRepository.save(pedido);
        } catch (Exception e) {
            logger.error("Erro ao finalizar o pedido com ID: {}", pedidoId, e);
            throw new RuntimeException("Erro ao finalizar pedido", e);
        }
    }

    @PostMapping("/copiar/{pedidoId}")
    public Pedido copiarPedido(@PathVariable Long pedidoId) {
        logger.info("Copiando pedido com ID: {}", pedidoId);

        Pedido pedidoExistente = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    logger.error("Pedido com ID {} não encontrado", pedidoId);
                    return new RuntimeException("Pedido não encontrado");
                });

        try {
            Pedido novoPedido = new Pedido();
            novoPedido.setCliente(pedidoExistente.getCliente());
            novoPedido.setDataPedido(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            for (Produto produto : pedidoExistente.getProdutos()) {
                novoPedido.adicionarProduto(produto);
            }
            logger.info("Pedido copiado com sucesso: {}", pedidoId);
            return pedidoRepository.save(novoPedido);
        } catch (Exception e) {
            logger.error("Erro ao copiar o pedido com ID: {}", pedidoId, e);
            throw new RuntimeException("Erro ao copiar pedido", e);
        }
    }
}

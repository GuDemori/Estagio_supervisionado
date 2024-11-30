package controllers;

import entities.Cliente;
import entities.Pedido;
import entities.Produto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import repository.ClienteRepository;
import repository.PedidoRepository;
import repository.ProdutoRepository;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping
    public Cliente criarCliente(@RequestBody Cliente cliente){
        try{
            logger.info("Cliente criado com sucesso com id {}", cliente.getId());
            return clienteRepository.save(cliente);
        }catch (Exception e){
            logger.error("Erro ao criar cliente", e);
            throw new RuntimeException("Falha ao criar cliente");
        }
    }

    @GetMapping("/{id}")
    public Cliente getCliente (@PathVariable Long id){
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não econtrado"));
    }

    @PutMapping("/{id}")
    public Cliente atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente){
        if (!clienteRepository.existsById(id)){
            logger.error("Erro ao encontrar clinte com id {}", id);
            throw new RuntimeException("Cliente não encontrado");
        }
        try{
            cliente.setId(id);
            logger.info("Cliente atualizado");
            return clienteRepository.save(cliente);
        }
        catch (Exception e){
            logger.error("Erro ao atualizar cliente", e);
            throw new RuntimeException("Não foi possível atualizar o cliente");
        }
    }

    @DeleteMapping
    public void deletarCliente(@PathVariable Long id){
        if (!clienteRepository.existsById(id)){
            logger.error("Não foi possível encontrar o cliente de id {}", id);
            throw new RuntimeException("Não foi possível encontrar o cliente");
        }
        try{
            clienteRepository.deleteById(id);
            logger.info("Cliente de id {} deletado", id);
        }catch (Exception e) {
            logger.error("Erro ao deletar o cliente de id", id);
            throw new RuntimeException("Não foi possível deletar cliente", e);
        }
    }

    @GetMapping("/produtos")
    public List<Produto> consultarProdutos(){
        return produtoRepository.findAll();
    }

    @PostMapping("/pedidos")
    public Pedido criarPedido(@PathVariable Long clienteId, @RequestBody Pedido pedido){
        if (!clienteRepository.existsById(clienteId)){
            logger.error("Erro ao encontrar o cliente com id {}", clienteId);
            throw new RuntimeException("Não foi possível encotnrar o cliente");
        }try{
            pedido.setCliente(clienteRepository.findById(clienteId).get());
            logger.info("Pedido {} criado para o cliente {}", pedido.getId(), clienteId);
            return pedidoRepository.save(pedido);
        }catch (Exception e){
            logger.error("Erro ao criar pedido {} para o cliente {}", pedido.getId(), clienteId);
            throw new RuntimeException("Não foi possível criar o pedido");
        }

    }

}

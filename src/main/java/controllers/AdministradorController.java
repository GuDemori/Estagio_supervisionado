package controllers;

import entities.Administrador;
import entities.Cliente;
import entities.Pedido;
import entities.Produto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.AdministradorRepository;
import repository.ClienteRepository;
import repository.PedidoRepository;
import repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/administrador")
public class AdministradorController {

    private static final Logger logger = LoggerFactory.getLogger(AdministradorController.class);

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/clientes")
    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }

    @PostMapping("/clientes")
    public Cliente criarCliente(@RequestBody Cliente cliente){
        return clienteRepository.save(cliente);
    }

    @PostMapping
    public Administrador criarAdministrador(@RequestBody Administrador administrador){
        try{
            logger.info("Administrador criado com nome {}", administrador.getNome());
           return administradorRepository.save(administrador);
        }catch (Exception e){
            logger.error("Erro ao criar administrador");
            throw new RuntimeException("Falha ao criar administrador");
        }
    }

    @GetMapping("/{id}")
    public Administrador getAdminisstrador(@PathVariable Long id){
        return administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado"));

    }

    @PutMapping("/{id}")
    public Administrador atualizarAdministrador(@PathVariable Long id, @RequestBody Administrador administrador){
        if (!administradorRepository.existsById(id)){
            logger.error("Administrador não encontrado");
            throw new RuntimeException("Administrador não encontrado");
        }
        try {
            administrador.setId(id);
            logger.info("Administraodor {} atualizado", administrador.getNome());
            return administradorRepository.save(administrador);
        }catch (Exception e){
            logger.error("Erro ao atualizar o administrador", e);
            throw new RuntimeException("Falha ao atualizar o administrador");
        }
    }

    @DeleteMapping
    public void deletarAdministrador(@PathVariable Long id){
        if (!administradorRepository.existsById(id)){
            logger.error("Administrador não encontrado");
            throw new RuntimeException("Administrador não encontrado");
        }
        try{
            administradorRepository.deleteById(id);
            logger.info("Administrador deletado");
        } catch (Exception e){
            logger.error("Erro ao deletar administrador");
            throw new RuntimeException("Falha ao deletar administrador");
        }
    }


    @PutMapping("/cliente/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado){
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNomeEstabelecimento(clienteAtualizado.getNomeEstabelecimento());
                    cliente.setLogin(clienteAtualizado.getLogin());
                    return ResponseEntity.ok(cliente);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/cliente/{id}")
    public ResponseEntity<Object> deletarCliente(@PathVariable Long id){
        return clienteRepository.findById(id)
                .map(cliente -> {
                    clienteRepository.delete(cliente);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/produtos")
    public List<Produto> listarProdutos(){
        logger.info("Produtos foram listados");
        return produtoRepository.findAll();
    }

    @PostMapping("/produtos")
    public Produto criarProduto(@RequestBody Produto produto){
      logger.info("Produto criado");
      return produtoRepository.save(produto);
    }

    @PutMapping("/produtos/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado){
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNomeProduto(produtoAtualizado.getNomeProduto());
                    produto.setNomeFornecedor(produtoAtualizado.getNomeFornecedor());
                    produto.setPreco(produtoAtualizado.getPreco());
                    produto.setDescricao(produtoAtualizado.getDescricao());
                    produto.setSabor(produtoAtualizado.getSabor());
                    produtoRepository.save(produto);
                    logger.info("Produto atualizado");
                    return ResponseEntity.ok(produto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/produtos/{id}")
    public ResponseEntity<Object> deletarProduto(@PathVariable Long id){
        return produtoRepository.findById(id)
                .map(produto -> {
                    produtoRepository.delete(produto);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/pedidos/{clienteId}/pedidos")
    public Pedido criarPedidoParaCliente(@PathVariable Long clienteId, @RequestBody Pedido pedido){
        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        if (cliente.isEmpty()){
            logger.error("Erro ao encontrar cliente");
            throw new RuntimeException("Cliente não encontrado");
        }
        try{
            pedido.setCliente(cliente.get());
            return pedidoRepository.save(pedido);
        }catch (Exception e){
            logger.error("Erro ao criar pedido");
            throw new RuntimeException("Falha ao criar pedido");
        }

    }

}








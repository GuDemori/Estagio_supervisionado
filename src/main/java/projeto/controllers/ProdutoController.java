package projeto.controllers;

import projeto.entities.Produto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projeto.repository.ProdutoRepository;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    public Produto criarPoduto(@RequestBody Produto produto) {
        try {
            return produtoRepository.save(produto);
        } catch (Exception e) {
            logger.error("Erro ao criar produto");
            throw new RuntimeException("Falha ao criar produto");
        }
    }

    @GetMapping("/{id}")
    public Produto getProduto(@PathVariable Long id){
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    @PutMapping("/{id}")
    public Produto atualizarProduto(@PathVariable Long id, @RequestBody Produto produto){
        if(!produtoRepository.existsById(id)){
            logger.warn("Tentativa de encontrar produto não existente");
            throw new RuntimeException("Produto não encontrado");
        }
        try {
            produto.setId(id);
            logger.info("Produto salvo com sucesso");
            return produtoRepository.save(produto);
        }catch (Exception e){
            logger.error("Erro ao atualizar produto");
            throw new RuntimeException("Não foi possível atualizar o produto");
        }
    }

    @DeleteMapping("/{id}")
    public void deletarProduto(@PathVariable Long id){
        if (!produtoRepository.existsById(id)){
            logger.error("Erro ao buscar produto com id {}", id);
            throw new RuntimeException("Não foi possível econtrar o produto " + id);
        }
        try{
            produtoRepository.deleteById(id);
        }catch (Exception e){
            logger.error("Erro ao deletar produto com id", e);
            throw new RuntimeException("Erro ao deletar produto com id " + id);
        }
    }

    @GetMapping
    public List<Produto> listarProdutos(){
        return produtoRepository.findAll();
    }

}


package controllers;

import entities.Fornecedor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import repository.FornecedorRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    private static final Logger logger = LoggerFactory.getLogger(FornecedorController.class);

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @GetMapping
    public List<Fornecedor> listarFornecedores(){
        logger.info("Listando todos os fornecedores");
        return fornecedorRepository.findAll();
    }

    @GetMapping
    public Fornecedor fornecedorPorId(@PathVariable Long fornecedorId){
        logger.info("Buscando fornecedor com Id {}", fornecedorId);

        return fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> {
                    logger.error("Fornecedor com id {} não encontrado", fornecedorId);
                    return new RuntimeException("Fornecedor não encontrado");
                });
    }

    @PostMapping
    public Fornecedor criarFornecedor(@PathVariable Long fornecedorId, @RequestBody Fornecedor fornecedor){
        logger.info("Criando um novo fornecedor {}", fornecedor.getNomeFornecedor());

        try {
            Fornecedor novoFornecedor = fornecedorRepository.save(fornecedor);
            logger.info("Fornecedor criado com sucesso");
            return novoFornecedor;
        }catch (Exception e){
            logger.error("Erro ao criar fornecedor: {}", fornecedor.getNomeFornecedor());
            throw new RuntimeException("Erro ao criar fornecedor", e);
        }
    }

    @PutMapping
    public Fornecedor atualizarFornecedor(@PathVariable Long fornecedorId, @RequestBody Fornecedor fornecedor){

        logger.info("Atualizando fornecedor com ID {}", fornecedorId);

        Optional<Fornecedor> fornecedorExistente = fornecedorRepository.findById(fornecedorId);

        if (fornecedorExistente.isEmpty()){
            logger.error("Fornecedor com id {} não encontrado", fornecedorId);
            throw new RuntimeException("Fornecedor não econtrado");
        }

        try {
            fornecedor.setId(fornecedorId);
            Fornecedor fornecedorAtualizado = fornecedorRepository.save(fornecedor);
            logger.info("Fornecedor atualizado com sucesso");
            return fornecedorAtualizado;
        }catch (Exception e){
            logger.error("Erro ao atualizar fornecedor com id {}", fornecedorId, e);
            throw new RuntimeException("Erro ao atualizar fornecedor");
        }
    }

    @DeleteMapping
    public void excluirFornecedor(@PathVariable Long fornecedorId){
        logger.info("Excluindo fornecedor com id {}", fornecedorId);

        try {
            fornecedorRepository.deleteById(fornecedorId);
            logger.info("Fornecedor excluido com sucesso", fornecedorId);
        } catch (Exception e){
            logger.error("Erro ao excluir fornecedor com id {}", fornecedorId);
            throw new RuntimeException("Erro ao excluir fornecedor");
        }
    }

}

package project.controllers;

import project.entities.Fornecedor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.repository.FornecedorRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);

    @Autowired
    private FornecedorRepository supplierRepository;

    /**
     * Lista todos os fornecedores.
     *
     * @return Lista de fornecedores.
     */
    @GetMapping
    public List<Fornecedor> listAllSuppliers() {
        logger.info("Listando todos os fornecedores.");
        return supplierRepository.findAll();
    }

    /**
     * Busca um fornecedor pelo ID.
     *
     * @param supplierId ID do fornecedor.
     * @return Fornecedor encontrado.
     */
    @GetMapping("/{supplierId}")
    public Fornecedor getSupplierById(@PathVariable Long supplierId) {
        logger.info("Buscando fornecedor com ID: {}", supplierId);

        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> {
                    logger.error("Fornecedor com ID {} não encontrado.", supplierId);
                    return new RuntimeException("Fornecedor não encontrado.");
                });
    }

    /**
     * Cria um novo fornecedor.
     *
     * @param supplier Dados do fornecedor.
     * @return Fornecedor criado.
     */
    @PostMapping
    public Fornecedor createSupplier(@RequestBody Fornecedor supplier) {
        logger.info("Criando novo fornecedor: {}", supplier.getNomeFornecedor());

        try {
            return supplierRepository.save(supplier);
        } catch (Exception e) {
            logger.error("Erro ao criar fornecedor: {}", supplier.getNomeFornecedor(), e);
            throw new RuntimeException("Erro ao criar fornecedor.", e);
        }
    }

    /**
     * Atualiza os dados de um fornecedor.
     *
     * @param supplierId ID do fornecedor.
     * @param supplier   Dados atualizados do fornecedor.
     * @return Fornecedor atualizado.
     */
    @PutMapping("/{supplierId}")
    public Fornecedor updateSupplier(@PathVariable Long supplierId, @RequestBody Fornecedor supplier) {
        logger.info("Atualizando fornecedor com ID: {}", supplierId);

        supplierRepository.findById(supplierId)
                .orElseThrow(() -> {
                    logger.error("Fornecedor com ID {} não encontrado.", supplierId);
                    return new RuntimeException("Fornecedor não encontrado.");
                });

        supplier.setId(supplierId);

        try {
            return supplierRepository.save(supplier);
        } catch (Exception e) {
            logger.error("Erro ao atualizar fornecedor com ID: {}", supplierId, e);
            throw new RuntimeException("Erro ao atualizar fornecedor.", e);
        }
    }

    /**
     * Exclui um fornecedor pelo ID.
     *
     * @param supplierId ID do fornecedor.
     */
    @DeleteMapping("/{supplierId}")
    public void deleteSupplier(@PathVariable Long supplierId) {
        logger.info("Excluindo fornecedor com ID: {}", supplierId);

        supplierRepository.findById(supplierId)
                .orElseThrow(() -> {
                    logger.error("Fornecedor com ID {} não encontrado.", supplierId);
                    return new RuntimeException("Fornecedor não encontrado.");
                });

        try {
            supplierRepository.deleteById(supplierId);
            logger.info("Fornecedor com ID {} excluído com sucesso.", supplierId);
        } catch (Exception e) {
            logger.error("Erro ao excluir fornecedor com ID: {}", supplierId, e);
            throw new RuntimeException("Erro ao excluir fornecedor.", e);
        }
    }
}

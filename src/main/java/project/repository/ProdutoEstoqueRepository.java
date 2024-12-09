package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.ProdutoEstoque;

public interface ProdutoEstoqueRepository extends JpaRepository<ProdutoEstoque, Long> {
}

package projeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.entities.ProdutoEstoque;

public interface ProdutoEstoqueRepository extends JpaRepository<ProdutoEstoque, Long> {
}

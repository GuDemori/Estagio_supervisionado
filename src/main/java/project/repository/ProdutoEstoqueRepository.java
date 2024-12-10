package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.ProductStock;

public interface ProdutoEstoqueRepository extends JpaRepository<ProductStock, Long> {
}

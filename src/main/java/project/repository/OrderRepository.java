package project.repository;

import project.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
        List<Order> findByClienteId(Long clienteId);
        /**
         * Busca todos os pedidos associados a um cliente pelo ID do cliente.
         *
         * @param clientId ID do cliente.
         * @return Lista de pedidos do cliente.
         */
        List<Order> findByClientId(Long clientId);

}

package project.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PEDIDO")
public class Order {

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEDIDO_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private Client client;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PEDIDO_ID")
    private List<Product> products = new ArrayList<>();

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Autowired
    public Order(Client client, String status){
        this.orderDate = LocalDate.now();
        this.client = client;
        this.status = status;
        logger.info("Pedido criado para o cliente: {}", client.getEstablishmentName());
        this.products = new ArrayList<>();
    }

    public Client getClient() {
        return client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setOrderDate(LocalDate orderDate) {
        if (orderDate == null || orderDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data do pedido não pode ser nula ou no futuro.");
        }
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order(){

    }

    public List<Product> getProducts() {
        return products;
    }


    public void addProduct(Product product){
        if(product == null){
            logger.error("Tentativa de adicionar um produto nulo ao pedido");
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        products.add(product);
        logger.info("Produto adicionado ao pedido: {}", product.getProductName());
    }

    public double getTotal() {
        try {
            // Soma o total baseado no preço e quantidade de cada produto no pedido
            double total = products.stream()
                    .mapToDouble(product -> product.getPrice() * product.getQuantity())
                    .sum();
            logger.info("Valor total calculado para o pedido com ID {}: {}", id, total);
            return total;
        } catch (Exception e) {
            logger.error("Erro ao calcular o total para o pedido com ID {}", id, e);
            throw new RuntimeException("Erro ao calcular o total do pedido", e);
        }
    }

    @Transactional
    public void finalizeOrder(Stock stock) {
        try {
            for (Product product : products) {
                stock.updateStock(product, 1);
                logger.info("Stock updated for the product: {}", product.getProductName());
            }
            logger.info("Order finalized for {}", client.getEstablishmentName());
        } catch (Exception e) {
            logger.error("Error finalizing the order for the client: {}", client.getEstablishmentName());
            throw new RuntimeException("Error finalizing the order", e);
        }
    }

}


package project.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
    private Long Id;
    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private Client client;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PEDIDO_ID")
    private List<Product> products = new ArrayList<>();
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String orderDate;
    @Autowired
    public Order(Client client, String status){
        this.orderDate = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss"));
        this.client = client;
        this.status = status;
        logger.info("Pedido criado para o cliente: {}", client.getEstablishmentName());
        this.products = new ArrayList<>();
    }

    public Client getClient() {
        return client;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
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

    public double calcularValorTotal() {
        try {
            double valorTotal = products.stream()
                    .mapToDouble(produto -> produto.getPrice() * produto.getQuantity())
                    .sum();
            logger.info("Valor total calculado para o pedido: {}", valorTotal);
            return valorTotal;
        } catch (Exception e) {
            logger.error("Erro ao calcular valor total do pedido", e);
            throw new RuntimeException("Erro ao calcular o valor total do pedido", e);
        }
    }

    @Transactional
    public void finalizarPedido(Estoque estoque){
        try {
            for (Product product : products) {
                estoque.atualizarEstoque(product, 1);
                logger.info("Estoque atualizado para o produto: {}", product.getProductName());
            }
            logger.info("Pedido finalizado para {}", client.getEstablishmentName());
        }catch (Exception e){
            logger.error("Erro ao finalizar o pedido do cliente: {}", client.getEstablishmentName());
            throw new RuntimeException("Erro ao finalizar o pedido", e);
        }
    }

}


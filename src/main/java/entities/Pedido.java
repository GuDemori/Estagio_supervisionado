package entities;

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
public class Pedido {

    private static final Logger logger = LoggerFactory.getLogger(Pedido.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEDIDO_ID")
    private Long Id;
    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PEDIDO_ID")
    private List<Produto> produtos = new ArrayList<>();
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String dataPedido;
    @Autowired
    public Pedido(Cliente cliente, String status){
        this.dataPedido = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss"));
        this.cliente = cliente;
        this.status = status;
        logger.info("Pedido criado para o cliente: {}", cliente.getNomeEstabelecimento());
        this.produtos = new ArrayList<>();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(String dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Pedido(){

    }

    public List<Produto> getProdutos() {
        return produtos;
    }


    public void adicionarProduto(Produto produto){
        if(produto == null){
            logger.error("Tentativa de adicionar um produto nulo ao pedido");
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        produtos.add(produto);
        logger.info("Produto adicionado ao pedido: {}", produto.getNomeProduto());
    }

    public double calcularValorTotal() {
        try {
            double valorTotal = produtos.stream()
                    .mapToDouble(produto -> produto.getPreco() * produto.getQuantidade())
                    .sum();
            logger.info("Valor total calculado para o pedido: {}", valorTotal);
            return valorTotal;
        } catch (Exception e) {
            logger.error("Erro ao calcular valor total do pedido", e);
            throw new RuntimeException("Erro ao calcular o valor total do pedido", e);
        }
    }

    @Transactional
    public void finalizarPedido(){
        try {
            for (Produto produto : produtos) {
                estoque.atualizarEstoque(produto, 1);
                logger.info("Estoque atualizado para o produto: {}", produto.getNomeProduto());
            }
            logger.info("Pedido finalizado para {}", cliente.getNomeEstabelecimento());
        }catch (Exception e){
            logger.error("Erro ao finalizar o pedido do cliente: {}", cliente.getNomeEstabelecimento());
            throw new RuntimeException("Erro ao finalizar o pedido", e);
        }
    }

}


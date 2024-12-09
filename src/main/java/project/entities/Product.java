package project.entities;

import project.builders.ProductBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Entity
@Table(name = "PRODUTO")
public class Product {

    private static final Logger logger = LoggerFactory.getLogger(Product.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUTO_ID")
    private Long id;

    @NotBlank(message = "O nome do produto não pode estar vazio ou nulo.")
    @Column(name = "NOME_PRODUTO", nullable = false, length = 100)
    private String productName;

    @NotNull(message = "O preço não pode ser nulo.")
    @Min(value = 0, message = "O preço não pode ser negativo.")
    @Column(name = "PRECO", nullable = false)
    private double price;

    @Column(name = "DESCRICAO", length = 255)
    private String description;

    @NotNull(message = "A quantidade não pode ser nula.")
    @Min(value = 0, message = "A quantidade não pode ser negativa.")
    @Column(name = "QUANTIDADE")
    private int quantity;

    @Column(name = "SABOR", length = 50)
    private String flavor;

    // Comentado para a V2
    // @Column(name = "NOME_FORNECEDOR", length = 255)
    // private String supplierName;

    @ElementCollection
    @CollectionTable(name = "PRODUTO_APELIDOS", joinColumns = @JoinColumn(name = "PRODUTO_ID"))
    @Column(name = "APELIDO")
    private Set<String> nicknames = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "PRODUTO_IMAGENS", joinColumns = @JoinColumn(name = "PRODUTO_ID"))
    @MapKeyColumn(name = "SABOR")
    @Column(name = "IMAGEM_URL")
    private Map<String, String> flavorImages = new HashMap<>();

    public Product(String productName, double price, String description, int quantity, String flavor) {
        if (quantity < 0) {
            logger.error("Tentativa de criar produto {} com quantidade inferior a zero", productName);
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.flavor = flavor;
        logger.info("Produto criado: {}", productName);
    }

    public Product() {}

    public Set<String> getNicknames() {
        return nicknames;
    }

    public void addNickname(String nickname) {
        if (nickname != null && !nickname.isBlank() && !nicknames.contains(nickname)) {
            nicknames.add(nickname);
            logger.info("Apelido '{}' adicionado ao produto {}", nickname, productName);
        }
    }

    public void removeNickname(String nickname) {
        if (nicknames.remove(nickname)) {
            logger.info("Apelido '{}' removido do produto {}", nickname, productName);
        }
    }

    public boolean matchesNickname(String term) {
        return nicknames.stream().anyMatch(nickname -> nickname.toLowerCase().contains(term.toLowerCase()));
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            logger.error("Nome do produto não pode ser vazio ou nulo.");
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }
        this.productName = productName;
    }

    public void setPrice(double price) {
        if (price < 0) {
            logger.error("Preço do produto {} não pode ser negativo.", productName);
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            logger.error("Tentativa de atualizar produto {} com quantidade inferior a zero", productName);
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.quantity = quantity;
    }

    /**
     * Adiciona ou atualiza a imagem associada a um sabor específico do produto.
     *
     * @param flavor   O sabor do produto.
     * @param imageUrl A URL da imagem correspondente ao sabor  .
     * @throws IllegalArgumentException se o sabor ou a URL forem nulos ou vazios.
     */
    public void addFlavorImage(String flavor, String imageUrl) {
        if (flavor == null || flavor.trim().isEmpty() || imageUrl == null || imageUrl.trim().isEmpty()) {
            logger.error("Tentativa de adicionar imagem com dados inválidos: sabor={}, imagem={}.", flavor, imageUrl);
            throw new IllegalArgumentException("Sabor e URL da imagem são obrigatórios.");
        }
        flavorImages.put(flavor, imageUrl);
        logger.info("Imagem adicionada para o sabor {}: {}", flavor, imageUrl);
    }

    public String getImageByFlavor(String flavor) {
        return flavorImages.getOrDefault(flavor, "Imagem padrão");
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getFlavor() {
        return flavor;
    }

    public Map<String, String> getFlavorImages() {
        return flavorImages;
    }



    public Product reduceQuantity(int quantitySold) {
        if (quantitySold > this.quantity) {
            logger.error("Estoque insuficiente para o produto: {}. Quantidade em estoque: {}. Tentativa de redução: {}.",
                    productName, quantity, quantitySold);
            throw new IllegalArgumentException("Estoque insuficiente para " + getProductName());
        }

        this.quantity -= quantitySold;
        logger.info("Quantidade reduzida para o produto: {}. Nova quantidade em estoque: {}", productName, this.quantity);
        return this;
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }
}

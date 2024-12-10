package project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "PRODUCT_STOCK")
public class ProductStock {

    private static final Logger logger = LoggerFactory.getLogger(ProductStock.class);

    private static final String ERROR_NEGATIVE_QUANTITY = "Quantity cannot be negative.";
    private static final String ERROR_NEGATIVE_QUANTITY_LOG = "Quantity cannot be negative for products in stock. Product: {}. Stock: {}";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "STOCK_ID", nullable = false)
    private Stock stock;

    @NotNull
    @Min(0)
    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    /**
     * Default constructor for JPA
     */
    public ProductStock() {}

    /**
     * Complete constructor to initialize ProductStock
     *
     * @param product Product associated with the stock
     * @param stock Stock where the product is stored
     * @param quantity Initial quantity of the product
     * @throws IllegalArgumentException if quantity is negative
     */
    public ProductStock(Product product, Stock stock, int quantity) {
        validateQuantity(quantity);
        this.product = product;
        this.stock = stock;
        this.quantity = quantity;
        logger.info("Product {} created in stock {} with quantity {}",
                product != null ? product.getProductName() : "Unknown",
                stock != null ? stock.getId() : "Unknown",
                quantity);
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Validates if the quantity is positive
     *
     * @param quantity Quantity to be validated
     * @throws IllegalArgumentException if quantity is less than 0
     */
    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            logError();
            throw new IllegalArgumentException(ERROR_NEGATIVE_QUANTITY);
        }
    }

    /**
     * Logs information messages
     *
     * @param message Message for the log
     */
    private void logInfo(String message) {
        logger.info("{} Product: {}. Stock: {}. Quantity: {}.",
                message,
                product != null ? product.getProductName() : "Unknown",
                stock != null ? stock.getId() : "Unknown",
                quantity);
    }

    /**
     * Logs error messages
     */
    private void logError() {
        logger.error(ERROR_NEGATIVE_QUANTITY_LOG,
                product != null ? product.getProductName() : "Unknown",
                stock != null ? stock.getId() : "Unknown",
                quantity);
    }

    /**
     * Returns a textual representation of the ProductStock entity
     *
     * @return String with ProductStock details
     */
    @Override
    public String toString() {
        return String.format("ProductStock[id=%d, product=%s, stock=%s, quantity=%d]",
                id,
                product != null ? product.getProductName() : "Unknown",
                stock != null ? stock.getId() : "Unknown",
                quantity);
    }

    /**
     * Updates the quantity of the product in stock
     *
     * @param quantity New quantity to be set
     */
    public void setQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
        logInfo("Quantity updated for the product in stock.");
    }
}

package project.entities;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Entity
@Table(name = "STOCK")
public class Stock {

    private static final Logger logger = LoggerFactory.getLogger(Stock.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STOCK_ID")
    private Long id;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductStock> products;

    public Stock() {}

    /**
     * Adds a new product to the stock.
     *
     * @param product Product to be added
     * @param quantity Initial quantity of the product
     */
    public void addProduct(Product product, int quantity) {
        validateProductAndQuantity(product, quantity);

        ProductStock productStock = new ProductStock(product, this, quantity);
        products.add(productStock);

        logger.info("Product {} added to stock with quantity {}",
                product.getProductName(), quantity);
    }

    /**
     * Updates the stock for an existing product.
     *
     * @param product Product to be updated
     * @param quantitySold Quantity sold
     */
    public void updateStock(Product product, int quantitySold) {
        validateProductAndQuantity(product, quantitySold);

        ProductStock productStock = findProductStock(product);

        int newQuantity = productStock.getQuantity() - quantitySold;
        if (newQuantity < 0) {
            String errorMessage = String.format(
                    "Insufficient stock for product: %s. Current quantity: %d, Attempted sale: %d",
                    product.getProductName(), productStock.getQuantity(), quantitySold);
            logger.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        productStock.setQuantity(newQuantity);
        logger.info("Stock updated for product {}: new quantity: {}",
                product.getProductName(), newQuantity);
    }

    /**
     * Displays all products and their quantities in stock.
     */
    public void displayStock() {
        products.forEach(productStock ->
                logger.info("Product: {}, Stock: {}",
                        productStock.getProduct().getProductName(),
                        productStock.getQuantity()));
    }

    /**
     * Validates the product and quantity.
     *
     * @param product Product to be validated
     * @param quantity Quantity to be validated
     */
    private void validateProductAndQuantity(Product product, int quantity) {
        if (product == null || quantity < 0) {
            throw new IllegalArgumentException("Invalid product or quantity");
        }
    }

    /**
     * Finds a ProductStock by the associated product.
     *
     * @param product Product to be searched
     * @return ProductStock associated with the product
     */
    private ProductStock findProductStock(Product product) {
        return products.stream()
                .filter(ps -> ps.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found in stock: " + product.getProductName()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductStock> getProducts() {
        return products;
    }

    public void setProducts(List<ProductStock> products) {
        this.products = products;
    }
}

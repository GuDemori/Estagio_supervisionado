package project.builders;

import project.entities.Product;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProductBuilder {

    private String productName;
    private String description;
    private double price;
    private int quantity;
    private String flavor = "";
    private String supplierName;
    private Set<String> nicknames = new HashSet<>();
    private Map<String, String> flavorImages = new HashMap<>();

    public ProductBuilder setProductName(String productName) {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("O produto deve ter um nome válido");
        }
        this.productName = productName;
        return this;
    }

    public ProductBuilder setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("A descrição do produto é obrigatória");
        }
        this.description = description;
        return this;
    }

    public ProductBuilder setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero");
        }
        this.price = price;
        return this;
    }

    public ProductBuilder setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa");
        }
        this.quantity = quantity;
        return this;
    }

    public ProductBuilder setFlavor(String flavor) {
        this.flavor = flavor;
        return this;
    }

//    public ProductBuilder setSupplierName(String supplierName) {
//        this.supplierName = supplierName;
//        return this;
//    }

    public ProductBuilder addNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("O apelido não pode ser vazio ou nulo");
        }
        this.nicknames.add(nickname);
        return this;
    }

    public ProductBuilder addFlavorImage(String flavor, String imageUrl) {
        if (flavor == null || flavor.trim().isEmpty()) {
            throw new IllegalArgumentException("O sabor não pode ser vazio ou nulo");
        }
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("A URL da imagem não pode ser vazia ou nula");
        }
        this.flavorImages.put(flavor, imageUrl);
        return this;
    }

    public Product build() {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalStateException("O produto deve ter um nome");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalStateException("A descrição do produto é obrigatória");
        }
        if (price <= 0) {
            throw new IllegalStateException("O preço deve ser maior que zero");
        }
        if (quantity < 0) {
            throw new IllegalStateException("A quantidade não pode ser negativa");
        }

        Product product = new Product(productName, price, description, quantity, flavor);
        product.getNicknames().addAll(nicknames); // Adiciona apelidos ao produto
        product.getFlavorImages().putAll(flavorImages); // Adiciona imagens por sabor
//        product.setSupplierName(supplierName); // Define o nome do fornecedor, se houver
        return product;
    }
}

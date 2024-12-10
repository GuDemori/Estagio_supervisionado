package project.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.dto.ProductReportDTO;
import project.entities.Product;
import project.repository.ProductRepository;

import java.util.List;

public class ProductReportService {

    private final ProductRepository productRepository;

    public ProductReportService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/relatorio/produtos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductReportDTO>> getProductsReport(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String nickname) {

        List<Product> products = productRepository.findAll();

        // Aplicação dos filtros
        if (name != null) {
            products = products.stream()
                    .filter(product -> product.getProductName().equalsIgnoreCase(name))
                    .toList();
        }

        if (nickname != null) {
            products = products.stream()
                    .filter(product -> product.getNicknames().contains(nickname))
                    .toList();
        }

        // Transformando para DTO
        List<ProductReportDTO> productReports = products.stream().map(product -> {
            ProductReportDTO dto = new ProductReportDTO();
            dto.setProductName(product.getProductName());
            dto.setPrice(product.getPrice());
            dto.setQuantity(product.getQuantity());
            dto.setFlavor(product.getFlavor());
            return dto;
        }).toList();

        return ResponseEntity.ok(productReports);
    }

}

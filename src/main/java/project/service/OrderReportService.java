package project.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.dto.OrderReportDTO;
import project.entities.Order;
import project.repository.OrderRepository;

import java.util.List;

public class OrderReportService {

    private final OrderRepository orderRepository;

    public OrderReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/relatorio/pedidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderReportDTO>> getOrdersReport(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String city) {

        List<Order> orders = orderRepository.findAll();

        if (date != null) {
            orders = orders.stream()
                    .filter(order -> order.getOrderDate().toString().equals(date))
                    .toList();
        }

        if (clientId != null) {
            orders = orders.stream()
                    .filter(order -> order.getClient().getId().equals(clientId))
                    .toList();
        }

        if (productName != null) {
            orders = orders.stream()
                    .filter(order -> order.getProducts().stream()
                            .anyMatch(product -> product.getProductName().equalsIgnoreCase(productName)))
                    .toList();
        }

        if (city != null) {
            orders = orders.stream()
                    .filter(order -> order.getClient().getCity().equalsIgnoreCase(city))
                    .toList();
        }

        // Transformando para DTO
        List<OrderReportDTO> orderReports = orders.stream().map(order -> {
            OrderReportDTO dto = new OrderReportDTO();
            dto.setOrderId(order.getId());
            dto.setOrderDate(order.getOrderDate());
            dto.setClientName(order.getClient().getEstablishmentName());
            dto.setCity(order.getClient().getCity());
            dto.setTotal(order.getTotal());
            return dto;
        }).toList();

        return ResponseEntity.ok(orderReports);
    }

}

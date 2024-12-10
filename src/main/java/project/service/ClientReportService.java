package project.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.dto.ClientReportDTO;
import project.entities.Client;
import project.repository.ClientRepository;

import java.util.List;

public class ClientReportService {

    private final ClientRepository clientRepository;

    public ClientReportService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/relatorio/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClientReportDTO>> getClientsReport(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String establishmentType) {

        List<Client> clients = clientRepository.findAll();

        // Aplicação dos filtros
        if (name != null) {
            clients = clients.stream()
                    .filter(client -> client.getEstablishmentName().equalsIgnoreCase(name))
                    .toList();
        }

        if (city != null) {
            clients = clients.stream()
                    .filter(client -> client.getCity().equalsIgnoreCase(city))
                    .toList();
        }

        if (establishmentType != null) {
            clients = clients.stream()
                    .filter(client -> client.getEstablishmentType().equalsIgnoreCase(establishmentType))
                    .toList();
        }

        // Transformando para DTO
        List<ClientReportDTO> clientReports = clients.stream().map(client -> {
            ClientReportDTO dto = new ClientReportDTO();
            dto.setName(client.getEstablishmentName());
            dto.setCity(client.getCity());
            dto.setAddress(client.getAddress());
            dto.setOrders(client.getOrders().size());
            return dto;
        }).toList();

        return ResponseEntity.ok(clientReports);
    }


}

package project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Client extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "O nome do estabelecimento não pode ser vazio ou nulo.")
    @Size(max = 255, message = "O nome do estabelecimento não pode ter mais que 255 caracteres.")
    @Column(name = "nome_estabelecimento", nullable = false)
    private String establishmentName;

    @NotBlank(message = "A cidade não pode ser vazia ou nula.")
    @Size(max = 255, message = "A cidade não pode ter mais que 255 caracteres.")
    @Column(name = "cidade", nullable = false)
    private String city;

    @NotBlank(message = "O endereço não pode ser vazio ou nulo.")
    @Size(max = 255, message = "O endereço não pode ter mais que 255 caracteres.")
    @Column(name = "endereco", nullable = false)
    private String address;

    @NotNull(message = "O tipo de estabelecimento não pode ser nulo.")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_estabelecimento", nullable = false)
    private String establishmentType;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public Client() {}

    public List<Order> getOrders() {
        return orders;
    }

    /**
     *
     * @param establishmentName Nome do estabelecimento do cliente.
     * @param city Cidade do cliente.
     * @param address Endereço do cliente.
     * @param username Nome de usuário único.
     * @param password
     * @param documentoIdentificacao
     */
    public Client(String establishmentName, String city, String address, String username, String password, String documentoIdentificacao) {
        super(username, password, documentoIdentificacao);
        this.establishmentName = establishmentName;
        this.city = city;
        this.address = address;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public void setEstablishmentName(String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getEstablishmentType() {
        return establishmentType;
    }

    public void setEstablishmentType(String establishmentType) {
        this.establishmentType = establishmentType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nomeEstabelecimento='" + establishmentName + '\'' +
                ", cidade='" + city + '\'' +
                ", endereco='" + address + '\'' +
                ", login='" + getUsername() + '\'' +
                ", documentoIdentificacao='" + getIdentificationDocument() + '\'' +
                '}';
    }
}

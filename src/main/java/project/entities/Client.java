package project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Client extends User {

    private String establishmentName;
    private String city;
    private String address;

    public Client() {}

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

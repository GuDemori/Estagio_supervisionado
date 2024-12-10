package project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.validation.DocumentConstraint;
import project.validation.PasswordConstraint;

import java.util.Collection;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "O nome de usuário não pode estar em branco.")
    @Size(max = 50, message = "O nome de usuário não deve ultrapassar 50 caracteres.")
    @Column(name = "USERNAME", nullable = false, unique = true, length = 50)
    private String username;

    @PasswordConstraint
    @Size(min = 8, max = 255, message = "A senha deve ter entre 6 e 255 caracteres.")
    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;

    @DocumentConstraint
    @Size(max = 14, message = "O documento de identificação não deve ultrapassar 14 caracteres.")
    @Column(name = "IDENTIFICATION_DOCUMENT", nullable = false, unique = true, length = 14)
    private String identificationDocument;

    private String role;

    /**
     * Construtor completo para inicializar um usuário.
     *
     * @param username               Nome de usuário.
     * @param password               Senha do usuário.
     * @param identificationDocument Documento de identificação do usuário.
     */
    public User(String username, String password, String identificationDocument) {
        this.username = username;
        this.password = password;
        this.identificationDocument = identificationDocument;
    }

    public User() {
    }



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentificationDocument() {
        return identificationDocument;
    }

    public void setIdentificationDocument(String identificationDocument) {
        this.identificationDocument = identificationDocument;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }
}

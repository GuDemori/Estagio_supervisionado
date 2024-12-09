package project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "administrador")
public class Administrator extends User {

    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(max = 100, message = "O nome não deve ultrapassar 100 caracteres.")
    private String name;

    /**
     * Construtor completo para inicializar um Administrador.
     *
     * @param name                  Nome do administrador.
     * @param username              Nome de usuário do administrador (utilizado para login).
     * @param password              Senha do administrador.
     * @param identificationDocument Documento de identificação do administrador.
     */
    public Administrator(String name, String username, String password, String identificationDocument) {
        super(username, password, identificationDocument);
        this.name = name;
        setRole("ADMIN");
    }

    public Administrator() {
        setRole("ADMIN");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

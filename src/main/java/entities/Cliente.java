package entities;

import builders.ClienteBuilder;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;
    @Column(name = "NOME_ESTABELECIMENTO", nullable = false)
    private String nomeEstabelecimento;
    @Column(name = "CIDADE", length = 255, nullable = false)
    private String cidade;
    @Column(name = "ENDERECO", length = 255)
    private String endereco;
    @Column(name = "LOGIN", nullable = false)
    private String login;
    @Column(name = "SENHA", nullable = false, length = 255)
    private String senha;
    @Column(name = "DOCUMENTO_IDENTIFICACAO", nullable = false, length = 14)
    private String documentoIdentificacao;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.login;
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

    public boolean isEnabled(){
        return true;
    }

    public Cliente(String nomeEstabelecimento, String cidade, String endereco) {
        this.nomeEstabelecimento = nomeEstabelecimento;
        this.cidade = cidade;
        this.endereco = endereco;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setDocumentoIdentificacao(String documentoIdentificacao) {
        this.documentoIdentificacao = documentoIdentificacao;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getDocumentoIdentificacao() {
        return documentoIdentificacao;
    }

    public Long getId() {
        return id;
    }
    public String getCidade() { return cidade; }

    public String getEndereco() { return endereco; }

    public String getNomeEstabelecimento(){
        return nomeEstabelecimento;
    }

    public static ClienteBuilder builder(){
        return new ClienteBuilder();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeEstabelecimento(String nomeEstabelecimento) {
        this.nomeEstabelecimento = nomeEstabelecimento;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}

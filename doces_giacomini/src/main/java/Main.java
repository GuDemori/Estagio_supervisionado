import context.ConexaoBanco;
import entities.Administrador;
import entities.Cliente;
import entities.Estoque;
import entities.Produto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        Estoque estoque = new Estoque();
        SpringApplication.run(Main.class, args);
        Produto candudo_Frito = Produto.builder()
                .setNomeProduto("Canudo Frito")
                .setPreco(25.99)
                .setDescricao("Canudo Frito DuDota 8Un")
                .setQuantidade(95)
                .build();

        Produto bubbaloo = Produto.builder()
                .setNomeProduto("Bubbaloo Tutti-Frutti")
                .setPreco(29.99)
                .setDescricao("Caixa Bubbaloo 60Un Sabor Tutti-Frutti")
                .setQuantidade(80)
                .setSabor("Tutti-Frutti")
                .build();

        Administrador rodrigo = Administrador.builder()
                .setNome("Rodrigo Giacomini")
                .setLogin("rodrigo08@gmail.com")
                .setSenha("12345678")
                .setDocumentoIdentificacao("000000000000")
                .build();

        Cliente jMaia = Cliente.builder()
                .setNomeEstabelecimento("Supermercado JMaia")
                .setCidade("Indianópolis")
                .setEndereco("Av. Xavantes, 990")
                .build();
        setConexao();
    }

    public static void setConexao() {
        Connection conexao = ConexaoBanco.conectar();
        if (conexao != null) {
            System.out.println("Conectado ao banco de dados com sucesso!");
        } else {
            System.out.println("Falha na conexão com o banco de dados.");
        }
    }

}

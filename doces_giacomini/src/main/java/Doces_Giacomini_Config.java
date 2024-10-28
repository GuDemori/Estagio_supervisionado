import entities.Estoque;
import entities.Produto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Doces_Giacomini_Config {
    @Bean
    CommandLineRunner init(Estoque estoque) {
        return args -> {
            Produto candudo_Frito = Produto.builder()
                    .setNomeProduto("Canudo Frito")
                    .setPreco(25.99)
                    .setDescricao("Canudo Frito DuDota 8Un")
                    .setQuantidade(95)
                    .build();
            estoque.adicionarProduto(candudo_Frito, 95);
        };
    }
}


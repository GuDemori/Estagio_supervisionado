package project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // Ativa a segurança baseada em métodos
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // Ignorar CSRF em APIs públicas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Apenas admins podem acessar
                        .requestMatchers("/cliente/**").hasRole("CLIENT") // Apenas clientes podem acessar
                        .anyRequest().authenticated() // Todas as outras requisições precisam de autenticação
                )
                .formLogin(form -> form.loginPage("/login").permitAll()) // Configuração para login
                .logout(logout -> logout.logoutUrl("/logout").permitAll()); // Configuração para logout

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

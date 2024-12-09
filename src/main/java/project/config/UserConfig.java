package project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(new BCryptPasswordEncoder().encode("admin123"))
                .roles("ADMIN") // Define a role como ROLE_ADMIN
                .build();

        UserDetails client = User.builder()
                .username("client")
                .password(new BCryptPasswordEncoder().encode("client123"))
                .roles("CLIENT") // Define a role como ROLE_CLIENT
                .build();

        return new InMemoryUserDetailsManager(admin, client);
    }
}

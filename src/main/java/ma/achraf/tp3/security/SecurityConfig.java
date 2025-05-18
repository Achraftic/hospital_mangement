package ma.achraf.tp3.security;

import lombok.AllArgsConstructor;
import ma.achraf.tp3.security.service.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private PasswordEncoder passwordEncoder;
    private UserDetailServiceImpl userDetailServiceImpl;




   // @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
       return new JdbcUserDetailsManager(dataSource);

    }

   // @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("12345")).roles("USER").build(),
                User.withUsername("user2").password(passwordEncoder.encode("12345")).roles("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("12345")).roles("USER","ADMIN").build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/webjars/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .rememberMe(Customizer.withDefaults())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/notAuthorized")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .userDetailsService(userDetailServiceImpl)
                .build();
    }
}

 // @Bean : chaque methode utilise Bean c-a-d il va s'execute au demarrage
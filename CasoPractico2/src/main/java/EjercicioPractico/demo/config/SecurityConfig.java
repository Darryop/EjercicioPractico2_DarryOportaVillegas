package EjercicioPractico.demo.config;

/**
 *
 * @author darry
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Log para debugging
        System.out.println("=== BCryptPasswordEncoder creado ===");
        String testHash = encoder.encode("12345");
        System.out.println("Hash de prueba para '12345': " + testHash);
        System.out.println("Verificación: " + encoder.matches("12345", testHash));
        
        return encoder;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/profesor/**").hasAnyAuthority("PROFESOR", "ADMIN")
                .requestMatchers("/estudiante/**").hasAnyAuthority("ESTUDIANTE", "ADMIN", "PROFESOR")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Temporal para pruebas
        
        return http.build();
    }
}
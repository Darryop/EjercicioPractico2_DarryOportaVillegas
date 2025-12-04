/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EjercicioPractico.demo.test;

/**
 *
 * @author darry
 */

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordTest implements CommandLineRunner {
    
    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Hash que tienes en BD
        String bdHash = "$2a$10$UGTgVCpw42jvBAmrvnq7OuA/OKbR8V27KwKfRcMCLU1C1YycApSXW";
        
        System.out.println("=== PRUEBA DE CONTRASEÑA ===");
        System.out.println("Hash en BD: " + bdHash);
        System.out.println("Contraseña probada: 12345");
        
        boolean matches = encoder.matches("12345", bdHash);
        System.out.println("¿Coinciden? " + matches);
        
        if (!matches) {
            System.out.println("ERROR: El hash NO es para '12345'");
            // Generar uno nuevo
            String newHash = encoder.encode("12345");
            System.out.println("Nuevo hash para '12345': " + newHash);
            System.out.println("Usa este en la BD: UPDATE usuario SET password = '" + newHash + "'");
        }
    }
}

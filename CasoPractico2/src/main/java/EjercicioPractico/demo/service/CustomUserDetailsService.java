package EjercicioPractico.demo.service;

/**
 *
 * @author darry
 */


import EjercicioPractico.demo.domain.Usuario;
import EjercicioPractico.demo.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public CustomUserDetailsService(UsuarioRepository usuarioRepository, 
                                   BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("=== LOGIN INTENTO ===");
        System.out.println("Email: " + email);
        
        Usuario usuario = usuarioRepository.findByEmail(email);
        
        if (usuario == null) {
            System.out.println("ERROR: Usuario no encontrado en BD");
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }
        
        System.out.println("Usuario encontrado: " + usuario.getEmail());
        System.out.println("Hash en BD: " + usuario.getPassword());
        System.out.println("Longitud hash: " + (usuario.getPassword() != null ? usuario.getPassword().length() : 0));
        System.out.println("Activo: " + usuario.isActivo());
        
        // Verificar manualmente la contraseña
        boolean passwordOK = passwordEncoder.matches("12345", usuario.getPassword());
        System.out.println("¿Contraseña '12345' coincide con hash? " + passwordOK);
        
        if (!usuario.isActivo()) {
            System.out.println("ERROR: Usuario INACTIVO");
            throw new UsernameNotFoundException("Usuario inactivo");
        }
        
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRol().getNombre())
                .build();
    }
}
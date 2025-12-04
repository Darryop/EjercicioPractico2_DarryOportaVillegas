package EjercicioPractico.demo.service.impl;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Usuario;
import EjercicioPractico.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }
        
        // Convertir el rol de Usuario a GrantedAuthority
        // Asegúrate de que el nombre del rol empiece con "ROLE_"
        String role = usuario.getRol() != null ? 
            (usuario.getRol().getNombre().startsWith("ROLE_") ? 
                usuario.getRol().getNombre() : "ROLE_" + usuario.getRol().getNombre()) : 
            "ROLE_USER";
        
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(role)
        );
        
        return new User(
            usuario.getEmail(),
            usuario.getPassword(), // Asegúrate que esté encriptado con BCrypt
            usuario.isActivo(),    // enabled
            true,                  // accountNonExpired
            true,                  // credentialsNonExpired
            true,                  // accountNonLocked
            authorities
        );
    }
}
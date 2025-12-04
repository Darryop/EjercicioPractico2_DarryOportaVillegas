package EjercicioPractico.demo.service;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Rol;
import EjercicioPractico.demo.domain.Usuario;
import EjercicioPractico.demo.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RolService rolService;
    
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                             BCryptPasswordEncoder passwordEncoder,
                             RolService rolService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolService = rolService;
    }
    
    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    
    @Override
    public Usuario guardar(Usuario usuario) {
        // Si es un nuevo usuario (sin ID o ID = 0)
        if (usuario.getId() == null || usuario.getId() == 0) {
            // Validar que el email no exista
            Usuario existente = usuarioRepository.findByEmail(usuario.getEmail());
            if (existente != null) {
                throw new RuntimeException("El email ya está registrado: " + usuario.getEmail());
            }
            
            // Encriptar contraseña (si está vacía, usar 12345)
            String rawPassword = usuario.getPassword();
            if (rawPassword == null || rawPassword.trim().isEmpty()) {
                rawPassword = "12345"; // Contraseña por defecto
            }
            usuario.setPassword(passwordEncoder.encode(rawPassword));
            
            // Establecer fecha de creación
            usuario.setFechaCreacion(LocalDateTime.now());
            
        } else {
            // Usuario existente - obtener del repositorio
            Usuario existente = obtenerPorId(usuario.getId());
            
            // Validar cambio de email (si cambió)
            if (!existente.getEmail().equals(usuario.getEmail())) {
                Usuario conMismoEmail = usuarioRepository.findByEmail(usuario.getEmail());
                if (conMismoEmail != null && !conMismoEmail.getId().equals(usuario.getId())) {
                    throw new RuntimeException("El email ya está registrado por otro usuario: " + usuario.getEmail());
                }
            }
            
            // Manejo de contraseña
            String nuevaPassword = usuario.getPassword();
            if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
                // Mantener la contraseña actual
                usuario.setPassword(existente.getPassword());
            } else if (!passwordEncoder.matches(nuevaPassword, existente.getPassword())) {
                // Si la nueva contraseña es diferente, encriptarla
                usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            } else {
                // Misma contraseña, mantenerla
                usuario.setPassword(existente.getPassword());
            }
            
            // Mantener la fecha de creación original
            usuario.setFechaCreacion(existente.getFechaCreacion());
        }
        
        // Asignar rol si viene con ID
        if (usuario.getRol() != null && usuario.getRol().getId() != null) {
            Rol rol = rolService.obtenerPorId(usuario.getRol().getId());
            usuario.setRol(rol);
        }
        
        // Asegurar que activo tenga valor por defecto
        if (!usuario.isActivo()) {
            usuario.setActivo(false);
        } else {
            usuario.setActivo(true);
        }
        
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public Usuario obtenerPorId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            return usuario.get();
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
    }
    
    @Override
    public void eliminar(Long id) {
        // Verificar que el usuario existe
        Usuario usuario = obtenerPorId(id);
        
        // No permitir eliminar el último admin
        if (usuario.getRol().getNombre().equals("ADMIN")) {
            long totalAdmins = usuarioRepository.findAll().stream()
                    .filter(u -> u.getRol().getNombre().equals("ADMIN"))
                    .count();
            if (totalAdmins <= 1) {
                throw new RuntimeException("No se puede eliminar el único administrador del sistema");
            }
        }
        
        usuarioRepository.deleteById(id);
    }
    
    @Override
    public List<Usuario> buscarPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }
    
    @Override
    public List<Usuario> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return usuarioRepository.findByFechaCreacionBetween(inicio, fin);
    }
    
    @Override
    public List<Usuario> buscarPorCoincidencia(String texto) {
        return usuarioRepository.findByEmailContainingOrNombreContaining(texto, texto);
    }
    
    @Override
    public Map<String, Long> contarActivosInactivos() {
        Map<String, Long> estadisticas = new HashMap<>();
        
        try {
            long activos = usuarioRepository.countActivos();
            long inactivos = usuarioRepository.countInactivos();
            
            estadisticas.put("activos", activos);
            estadisticas.put("inactivos", inactivos);
        } catch (Exception e) {
            // Si fallan las consultas personalizadas, calcular manualmente
            List<Usuario> todos = usuarioRepository.findAll();
            long activos = todos.stream().filter(Usuario::isActivo).count();
            long inactivos = todos.size() - activos;
            
            estadisticas.put("activos", activos);
            estadisticas.put("inactivos", inactivos);
        }
        
        return estadisticas;
    }
    
    @Override
    public List<Usuario> listarOrdenadosPorFecha() {
        return usuarioRepository.findAllByOrderByFechaCreacionDesc();
    }
    
    // Método adicional para buscar por email
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    // Método para cambiar estado activo/inactivo
    public Usuario cambiarEstado(Long id, boolean activo) {
        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(activo);
        return usuarioRepository.save(usuario);
    }
    
    // Método para resetear contraseña
    public Usuario resetearPassword(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setPassword(passwordEncoder.encode("12345"));
        return usuarioRepository.save(usuario);
    }
    
    // Método para validar credenciales (usado en login)
    public boolean validarCredenciales(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return false;
        }
        return passwordEncoder.matches(password, usuario.getPassword()) && usuario.isActivo();
    }
}
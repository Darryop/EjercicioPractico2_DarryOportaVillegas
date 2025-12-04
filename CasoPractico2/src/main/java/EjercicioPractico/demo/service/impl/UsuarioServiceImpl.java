package EjercicioPractico.demo.service.impl;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Usuario;
import EjercicioPractico.demo.repository.UsuarioRepository;
import EjercicioPractico.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    
    @Override
    public Usuario guardar(Usuario usuario) {
        // Encriptar contraseña si es nueva o fue modificada
        if (usuario.getId() == null || usuario.getPassword().length() < 60) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
    
    @Override
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
    
    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    @Override
    public List<Usuario> buscarPorRol(String rolNombre) {
        return usuarioRepository.findByRolNombre(rolNombre);
    }
    
    @Override
    public long contarActivos() {
        return usuarioRepository.countByActivoTrue();
    }
    
    @Override
    public long contarInactivos() {
        return usuarioRepository.countByActivoFalse();
    }
}

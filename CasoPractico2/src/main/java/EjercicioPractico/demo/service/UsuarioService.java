package EjercicioPractico.demo.service;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Usuario;
import java.util.List;

public interface UsuarioService {
    List<Usuario> listarTodos();
    Usuario guardar(Usuario usuario);
    Usuario obtenerPorId(Long id);
    void eliminar(Long id);
    Usuario buscarPorEmail(String email);
    List<Usuario> buscarPorRol(String rolNombre);
    long contarActivos();
    long contarInactivos();
}
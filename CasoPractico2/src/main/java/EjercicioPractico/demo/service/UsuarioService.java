package EjercicioPractico.demo.service;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Rol;
import EjercicioPractico.demo.domain.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface UsuarioService {
    
    // CRUD básico
    List<Usuario> listarTodos();
    Usuario guardar(Usuario usuario);
    Usuario obtenerPorId(Long id);
    void eliminar(Long id);
    
    // Consultas personalizadas (requeridas en el ejercicio)
    List<Usuario> buscarPorRol(Rol rol);
    List<Usuario> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin);
    List<Usuario> buscarPorCoincidencia(String texto);
    Map<String, Long> contarActivosInactivos();
    List<Usuario> listarOrdenadosPorFecha();
    
    // Métodos adicionales útiles
    Usuario buscarPorEmail(String email);
    Usuario cambiarEstado(Long id, boolean activo);
    Usuario resetearPassword(Long id);
    boolean validarCredenciales(String email, String password);
}
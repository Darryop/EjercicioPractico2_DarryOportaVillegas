package EjercicioPractico.demo.service;

/**
 *
 * @author darry
 */
import EjercicioPractico.demo.domain.Rol;
import java.util.List;

public interface RolService {
    List<Rol> listarTodos();
    Rol guardar(Rol rol);
    Rol obtenerPorId(Long id);
    void eliminar(Long id);
    Rol buscarPorNombre(String nombre);
}
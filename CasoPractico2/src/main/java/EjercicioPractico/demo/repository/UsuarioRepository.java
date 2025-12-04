package EjercicioPractico.demo.repository;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Rol;
import EjercicioPractico.demo.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Consultas derivadas (requeridas)
    List<Usuario> findByRol(Rol rol);
    List<Usuario> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Usuario> findByEmailContainingOrNombreContaining(String email, String nombre);
    Usuario findByEmail(String email);
    
    // Consultas personalizadas con JPQL
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    Long countActivos();
    
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = false")
    Long countInactivos();
    
    // Consulta ordenada por fecha
    @Query("SELECT u FROM Usuario u ORDER BY u.fechaCreacion DESC")
    List<Usuario> findAllByOrderByFechaCreacionDesc();
    
    // Consulta por nombre o apellido
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(u.apellido) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Usuario> buscarPorTexto(@Param("texto") String texto);
}
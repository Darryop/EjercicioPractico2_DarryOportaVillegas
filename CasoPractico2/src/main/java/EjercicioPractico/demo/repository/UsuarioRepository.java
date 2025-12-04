package EjercicioPractico.demo.repository;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // 1. Buscar usuarios por rol
    List<Usuario> findByRolNombre(String nombreRol);
    
    // 2. Buscar usuarios creados en un rango de fechas
    List<Usuario> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // 3. Buscar usuarios por coincidencia parcial en correo o nombre
    List<Usuario> findByEmailContainingOrNombreContaining(String email, String nombre);
    
    // 4. Contar usuarios activos vs inactivos
    long countByActivoTrue();
    long countByActivoFalse();
    
    // 5. Obtener usuarios ordenados por fecha de creación
    List<Usuario> findAllByOrderByFechaCreacionDesc();
    
    // Consulta personalizada con @Query
    @Query("SELECT u FROM Usuario u WHERE u.email LIKE %:termino% OR u.nombre LIKE %:termino% OR u.apellido LIKE %:termino%")
    List<Usuario> buscarPorTermino(@Param("termino") String termino);
    
    // Buscar por email exacto
    Usuario findByEmail(String email);
    
  
}
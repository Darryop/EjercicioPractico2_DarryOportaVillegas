package EjercicioPractico.demo.repository;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findByNombre(String nombre);
}

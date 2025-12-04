package EjercicioPractico.demo.service;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Rol;
import EjercicioPractico.demo.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class RolServiceImpl implements RolService {
    
    private final RolRepository rolRepository;
    
    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }
    
    @Override
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }
    
    @Override
    public Rol guardar(Rol rol) {
        return rolRepository.save(rol);
    }
    
    @Override
    public Rol obtenerPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }
    
    @Override
    public void eliminar(Long id) {
        rolRepository.deleteById(id);
    }
    
    @Override
    public Rol buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
}
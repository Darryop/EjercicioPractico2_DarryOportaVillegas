package EjercicioPractico.demo.service.impl;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Rol;
import EjercicioPractico.demo.repository.RolRepository;
import EjercicioPractico.demo.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RolServiceImpl implements RolService {
    
    @Autowired
    private RolRepository rolRepository;
    
    @Override
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }
    
    @Override
    public Rol guardar(Rol rol) {
        // Validar que el nombre no esté vacío
        if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol no puede estar vacío");
        }
        
        // Convertir a mayúsculas para consistencia
        rol.setNombre(rol.getNombre().toUpperCase());
        
        return rolRepository.save(rol);
    }
    
    @Override
    public Rol obtenerPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }
    
    @Override
    public void eliminar(Long id) {
        // Verificar si el rol existe
        Rol rol = obtenerPorId(id);
        
        // Verificar si hay usuarios asociados a este rol
        if (!rol.getUsuarios().isEmpty()) {
            throw new IllegalStateException(
                "No se puede eliminar el rol '" + rol.getNombre() + 
                "' porque tiene usuarios asignados. Reasigna los usuarios primero."
            );
        }
        
        rolRepository.deleteById(id);
    }
    
    @Override
    public Rol buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre.toUpperCase());
    }
    
    @Override
    public boolean existePorNombre(String nombre) {
        return rolRepository.findByNombre(nombre.toUpperCase()) != null;
    }
    
    @Override
    public List<Rol> buscarRolesPorTermino(String termino) {
        // Implementación básica - puedes mejorar esto con una consulta personalizada
        List<Rol> todos = listarTodos();
        return todos.stream()
                .filter(rol -> rol.getNombre().toLowerCase().contains(termino.toLowerCase()) ||
                              (rol.getDescripcion() != null && 
                               rol.getDescripcion().toLowerCase().contains(termino.toLowerCase())))
                .toList();
    }
    
    @Override
    public long contarRoles() {
        return rolRepository.count();
    }
}

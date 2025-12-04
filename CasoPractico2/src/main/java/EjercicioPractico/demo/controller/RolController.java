package EjercicioPractico.demo.controller;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Rol;
import EjercicioPractico.demo.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/roles")
public class RolController {
    
    @Autowired
    private RolService rolService;
    
    @GetMapping
    public String listarRoles(Model model) {
        List<Rol> roles = rolService.listarTodos();
        model.addAttribute("roles", roles);
        model.addAttribute("titulo", "Gestión de Roles");
        return "admin/roles/listar";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("rol", new Rol());
        model.addAttribute("titulo", "Nuevo Rol");
        model.addAttribute("modo", "crear");
        return "admin/roles/formulario";
    }
    
    @PostMapping("/guardar")
    public String guardarRol(@ModelAttribute Rol rol, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si ya existe un rol con ese nombre
            if (rol.getId() == null && rolService.existePorNombre(rol.getNombre())) {
                redirectAttributes.addFlashAttribute("error", 
                    "Ya existe un rol con el nombre: " + rol.getNombre());
                return "redirect:/admin/roles/nuevo";
            }
            
            rolService.guardar(rol);
            redirectAttributes.addFlashAttribute("success", 
                "Rol guardado exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/roles";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Rol rol = rolService.obtenerPorId(id);
        model.addAttribute("rol", rol);
        model.addAttribute("titulo", "Editar Rol");
        model.addAttribute("modo", "editar");
        return "admin/roles/formulario";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarRol(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rolService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", 
                "Rol eliminado exitosamente");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Rol no encontrado");
        }
        
        return "redirect:/admin/roles";
    }
    
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Rol rol = rolService.obtenerPorId(id);
        model.addAttribute("rol", rol);
        model.addAttribute("titulo", "Detalle del Rol: " + rol.getNombre());
        return "admin/roles/detalle";
    }
}
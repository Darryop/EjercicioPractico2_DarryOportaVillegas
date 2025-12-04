package EjercicioPractico.demo.controller;

/**
 *
 * @author darry
 */


import EjercicioPractico.demo.domain.Usuario;
import EjercicioPractico.demo.service.UsuarioService;
import EjercicioPractico.demo.service.RolService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    private final RolService rolService;
    
    public UsuarioController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("roles", rolService.listarTodos());
        return "usuarios/list";
    }
    
    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolService.listarTodos());
        model.addAttribute("titulo", "Nuevo Usuario");
        return "usuarios/form";
    }
    
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.listarTodos());
        model.addAttribute("titulo", "Editar Usuario");
        return "usuarios/form";
    }
    
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        usuarioService.guardar(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario guardado exitosamente");
        return "redirect:/admin/usuarios";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        return "redirect:/admin/usuarios";
    }
}
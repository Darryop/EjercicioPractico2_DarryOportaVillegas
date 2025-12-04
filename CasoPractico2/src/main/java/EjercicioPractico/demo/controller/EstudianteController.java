package EjercicioPractico.demo.controller;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Usuario;
import EjercicioPractico.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/perfil")
    public String perfil(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        Usuario usuario = usuarioService.buscarPorEmail(email);
        
        model.addAttribute("titulo", "Mi Perfil");
        model.addAttribute("usuario", usuario);
        
        return "estudiante/perfil";
    }
}

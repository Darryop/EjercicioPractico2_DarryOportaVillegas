package EjercicioPractico.demo.controller;

/**
 *
 * @author darry
 */
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profesor")
public class ProfesorController {
    
    @GetMapping("/reportes")
    public String reportes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        model.addAttribute("titulo", "Reportes");
        model.addAttribute("username", username);
        model.addAttribute("rol", "PROFESOR");
        
        return "profesor/reportes";
    }
}

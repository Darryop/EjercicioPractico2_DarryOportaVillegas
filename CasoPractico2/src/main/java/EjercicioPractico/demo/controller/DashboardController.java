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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Obtener estadísticas
        List<Usuario> todosUsuarios = usuarioService.listarTodos();
        long totalUsuarios = todosUsuarios.size();
        long usuariosActivos = todosUsuarios.stream()
                .filter(Usuario::isActivo)
                .count();
        
        // Usuarios nuevos este mes
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        long nuevosEsteMes = todosUsuarios.stream()
                .filter(u -> u.getFechaCreacion() != null && u.getFechaCreacion().isAfter(inicioMes))
                .count();
        
        // Últimos 5 usuarios registrados
        List<Usuario> ultimosUsuarios = todosUsuarios.stream()
                .sorted((u1, u2) -> u2.getFechaCreacion().compareTo(u1.getFechaCreacion()))
                .limit(5)
                .collect(Collectors.toList());
        
        model.addAttribute("titulo", "Dashboard");
        model.addAttribute("username", username);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("usuariosActivos", usuariosActivos);
        model.addAttribute("totalRoles", 3); // Los 3 roles por defecto
        model.addAttribute("nuevosEsteMes", nuevosEsteMes);
        model.addAttribute("ultimosUsuarios", ultimosUsuarios);
        
        return "dashboard";
    }
}
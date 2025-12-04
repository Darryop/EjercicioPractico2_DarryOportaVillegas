package EjercicioPractico.demo.controller;

/**
 *
 * @author darry
 */
import EjercicioPractico.demo.domain.Usuario;
import EjercicioPractico.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/avanzadas")
    public String consultasAvanzadas(
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) String termino,
            Model model) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Consulta 1: Usuarios por rol
        List<Usuario> usuariosPorRol = null;
        if (rol != null && !rol.isEmpty()) {
            usuariosPorRol = usuarioService.buscarPorRol(rol);
        }
        
        // Consulta 2: Usuarios por rango de fechas
        List<Usuario> usuariosPorFecha = null;
        if (fechaInicio != null && fechaFin != null) {
            usuariosPorFecha = usuarioService.listarTodos().stream()
                    .filter(u -> u.getFechaCreacion() != null &&
                            u.getFechaCreacion().isAfter(fechaInicio) &&
                            u.getFechaCreacion().isBefore(fechaFin))
                    .toList();
        }
        
        // Consulta 3: Usuarios por término de búsqueda
        List<Usuario> usuariosPorTermino = null;
        if (termino != null && !termino.isEmpty()) {
            usuariosPorTermino = usuarioService.listarTodos().stream()
                    .filter(u -> u.getNombre().toLowerCase().contains(termino.toLowerCase()) ||
                            u.getApellido().toLowerCase().contains(termino.toLowerCase()) ||
                            u.getEmail().toLowerCase().contains(termino.toLowerCase()))
                    .toList();
        }
        
        // Consulta 4: Usuarios ordenados por fecha
        List<Usuario> usuariosOrdenados = usuarioService.listarTodos().stream()
                .sorted((u1, u2) -> u2.getFechaCreacion().compareTo(u1.getFechaCreacion()))
                .limit(10)
                .toList();
        
        // Estadísticas
        long totalActivos = usuarioService.contarActivos();
        long totalInactivos = usuarioService.contarInactivos();
        
        model.addAttribute("titulo", "Consultas Avanzadas");
        model.addAttribute("username", username);
        
        model.addAttribute("usuariosPorRol", usuariosPorRol);
        model.addAttribute("rolSeleccionado", rol);
        model.addAttribute("usuariosPorFecha", usuariosPorFecha);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("usuariosPorTermino", usuariosPorTermino);
        model.addAttribute("terminoBusqueda", termino);
        model.addAttribute("usuariosOrdenados", usuariosOrdenados);
        model.addAttribute("totalActivos", totalActivos);
        model.addAttribute("totalInactivos", totalInactivos);
        
        return "consultas/avanzadas";
    }
}
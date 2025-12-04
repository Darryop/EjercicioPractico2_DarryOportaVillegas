package EjercicioPractico.demo.controller;

/**
 *
 * @author darry
 */

import EjercicioPractico.demo.domain.Usuario;
import EjercicioPractico.demo.service.RolService;
import EjercicioPractico.demo.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final BCryptPasswordEncoder passwordEncoder;

    public HomeController(UsuarioService usuarioService,
            RolService rolService,
            BCryptPasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Buscar usuario por email
        Usuario usuario = usuarioService.listarTodos().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        // Estadísticas
        Map<String, Long> estadisticas = usuarioService.contarActivosInactivos();
        List<Usuario> ultimosUsuarios = usuarioService.listarOrdenadosPorFecha()
                .stream()
                .limit(5)
                .collect(Collectors.toList());

        long totalAdmins = usuarioService.listarTodos().stream()
                .filter(u -> u.getRol().getNombre().equals("ADMIN"))
                .count();
        
        long totalProfesores = usuarioService.listarTodos().stream()
                .filter(u -> u.getRol().getNombre().equals("PROFESOR"))
                .count();
        
        long totalEstudiantes = usuarioService.listarTodos().stream()
                .filter(u -> u.getRol().getNombre().equals("ESTUDIANTE"))
                .count();

        model.addAttribute("usuario", usuario);
        model.addAttribute("totalUsuarios", estadisticas.get("activos") + estadisticas.get("inactivos"));
        model.addAttribute("totalActivos", estadisticas.get("activos"));
        model.addAttribute("totalInactivos", estadisticas.get("inactivos"));
        model.addAttribute("totalAdmins", totalAdmins);
        model.addAttribute("totalProfesores", totalProfesores);
        model.addAttribute("totalEstudiantes", totalEstudiantes);
        model.addAttribute("ultimosUsuarios", ultimosUsuarios);

        if (usuario != null) {
            // Redirigir según rol
            String rol = usuario.getRol().getNombre();
            switch (rol) {
                case "ADMIN":
                    return "home-admin";
                case "PROFESOR":
                    return "home-profesor";
                case "ESTUDIANTE":
                    return "home-estudiante";
                default:
                    return "home";
            }
        }

        return "home";
    }

    // ========== PERFIL ==========
    @GetMapping("/perfil")
    public String perfil(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuario = usuarioService.listarTodos().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping("/perfil/actualizar")
    public String actualizarPerfil(@ModelAttribute Usuario usuarioForm,
            RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            Usuario usuarioActual = usuarioService.listarTodos().stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Actualizar solo los campos permitidos
            usuarioActual.setNombre(usuarioForm.getNombre());
            usuarioActual.setApellido(usuarioForm.getApellido());
            usuarioActual.setEmail(usuarioForm.getEmail());

            usuarioService.guardar(usuarioActual);

            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar perfil: " + e.getMessage());
        }

        return "redirect:/perfil";
    }

    @PostMapping("/perfil/cambiar-password")
    public String cambiarPassword(@RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            Usuario usuario = usuarioService.listarTodos().stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Validar contraseña actual
            if (!passwordEncoder.matches(currentPassword, usuario.getPassword())) {
                throw new RuntimeException("La contraseña actual es incorrecta");
            }

            // Validar que las nuevas contraseñas coincidan
            if (!newPassword.equals(confirmPassword)) {
                throw new RuntimeException("Las contraseñas no coinciden");
            }

            // Validar longitud mínima
            if (newPassword.length() < 5) {
                throw new RuntimeException("La contraseña debe tener al menos 5 caracteres");
            }

            // Actualizar contraseña
            usuario.setPassword(passwordEncoder.encode(newPassword));
            usuarioService.guardar(usuario);

            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("mensaje", "Contraseña cambiada exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("mensaje", "Error al cambiar contraseña: " + e.getMessage());
        }

        return "redirect:/perfil";
    }

    // ========== CONSULTAS ==========
    

    // ========== REPORTES ==========
    @GetMapping("/reportes")
    public String reportes(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        
        long totalActivos = usuarios.stream().filter(Usuario::isActivo).count();
        long totalInactivos = usuarios.size() - totalActivos;
        
        long totalAdmins = usuarios.stream()
                .filter(u -> u.getRol().getNombre().equals("ADMIN"))
                .count();
        
        long totalProfesores = usuarios.stream()
                .filter(u -> u.getRol().getNombre().equals("PROFESOR"))
                .count();
        
        long totalEstudiantes = usuarios.stream()
                .filter(u -> u.getRol().getNombre().equals("ESTUDIANTE"))
                .count();
        
        List<Usuario> ultimosUsuarios = usuarioService.listarOrdenadosPorFecha()
                .stream()
                .limit(10)
                .collect(Collectors.toList());

        model.addAttribute("totalUsuarios", usuarios.size());
        model.addAttribute("totalActivos", totalActivos);
        model.addAttribute("totalInactivos", totalInactivos);
        model.addAttribute("totalAdmins", totalAdmins);
        model.addAttribute("totalProfesores", totalProfesores);
        model.addAttribute("totalEstudiantes", totalEstudiantes);
        model.addAttribute("ultimosUsuarios", ultimosUsuarios);
        
        return "reportes";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
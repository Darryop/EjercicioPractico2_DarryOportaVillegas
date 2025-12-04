/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EjercicioPractico.demo.controller;

/**
 *
 * @author darry
 */


import EjercicioPractico.demo.domain.Usuario;
import EjercicioPractico.demo.domain.Rol;
import EjercicioPractico.demo.service.UsuarioService;
import EjercicioPractico.demo.service.RolService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {
    
    private final UsuarioService usuarioService;
    private final RolService rolService;
    
    public ConsultaController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }
    
    @GetMapping("/avanzadas")
    public String avanzadas(Model model) {
        model.addAttribute("roles", rolService.listarTodos());
        model.addAttribute("usuariosOrdenados", usuarioService.listarOrdenadosPorFecha());
        return "consultas/avanzadas";
    }
    
    @PostMapping("/buscarPorRol")
    public String buscarPorRol(@RequestParam Long rolId, Model model) {
        Rol rol = rolService.obtenerPorId(rolId);
        List<Usuario> usuarios = usuarioService.buscarPorRol(rol);
        
        model.addAttribute("usuariosPorRol", usuarios);
        model.addAttribute("rolSeleccionado", rol.getNombre());
        model.addAttribute("roles", rolService.listarTodos());
        model.addAttribute("usuariosOrdenados", usuarioService.listarOrdenadosPorFecha());
        return "consultas/avanzadas";
    }
    
    @PostMapping("/buscarPorTexto")
    public String buscarPorTexto(@RequestParam String texto, Model model) {
        List<Usuario> usuarios = usuarioService.buscarPorCoincidencia(texto);
        
        model.addAttribute("usuariosPorTexto", usuarios);
        model.addAttribute("textoBuscado", texto);
        model.addAttribute("roles", rolService.listarTodos());
        model.addAttribute("usuariosOrdenados", usuarioService.listarOrdenadosPorFecha());
        return "consultas/avanzadas";
    }
}
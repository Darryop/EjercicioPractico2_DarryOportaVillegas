/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EjercicioPractico.demo.controller;

/**
 *
 * @author darry
 */


import EjercicioPractico.demo.domain.Rol;
import EjercicioPractico.demo.service.RolService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/roles")
public class RolController {
    
    private final RolService rolService;
    
    public RolController(RolService rolService) {
        this.rolService = rolService;
    }
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("roles", rolService.listarTodos());
        return "roles/list";
    }
    
    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("rol", new Rol());
        model.addAttribute("titulo", "Nuevo Rol");
        return "roles/form";
    }
    
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Rol rol = rolService.obtenerPorId(id);
        model.addAttribute("rol", rol);
        model.addAttribute("titulo", "Editar Rol");
        return "roles/form";
    }
    
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Rol rol, RedirectAttributes redirectAttributes) {
        rolService.guardar(rol);
        redirectAttributes.addFlashAttribute("success", "Rol guardado exitosamente");
        return "redirect:/admin/roles";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        rolService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Rol eliminado exitosamente");
        return "redirect:/admin/roles";
    }
}
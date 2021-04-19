package com.dam.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dam.modelos.Usuario;
import com.dam.servicios.IServicioUsuario;

@Controller
@RequestMapping("/usuarios")
public class ControladorUsuario {

	@Autowired
	private IServicioUsuario servicioUsuario;

	@GetMapping("/index-usuarios")
	public String mostrarIndex(Model model) {
		List<Usuario> lista = servicioUsuario.findAll();
		model.addAttribute("usuarios", lista);
		return "usuarios/lista-usuarios";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable("id") int id, RedirectAttributes attributes) {
		servicioUsuario.delete(id);
		attributes.addFlashAttribute("message", "El usuario se ha eliminado correctamente");
		return "redirect:/usuarios/index";
	}

	/**
	 * Método para activar un usuario
	 * 
	 * @param idUsuario
	 * @param attributes
	 * @return
	 */
	@GetMapping("/unlock/{id}")
	public String activate(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
		servicioUsuario.activateUser(idUsuario);
		attributes.addFlashAttribute("message", "El usuario ha sido activado y ahora tiene acceso al sistema.");
		return "redirect:/usuarios/index-usuarios";
	}

	/**
	 * Método para bloquear un usuario
	 * 
	 * @param idUsuario
	 * @param attributes
	 * @return
	 */
	@GetMapping("/lock/{id}")
	public String block(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
		servicioUsuario.blockUser(idUsuario);
		attributes.addFlashAttribute("message", "El usuario ha sido bloqueado y no tiene acceso al sistema.");
		return "redirect:/usuarios/index-usuarios";
	}

}

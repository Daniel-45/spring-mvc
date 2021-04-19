package com.dam.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dam.modelos.Categoria;
import com.dam.servicios.IServicioCategoria;

@Controller
@RequestMapping("categorias")
public class ControladorCategoria {
	
	@Autowired
	private IServicioCategoria servicioCategoria;
	
	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Categoria> lista = servicioCategoria.findAll();
		model.addAttribute("categorias", lista);
		return "categorias/lista-categorias";
	}
	
	@GetMapping(value = "/index-categorias-paginado")
	public String showCategoriesPaginated(Model model, Pageable page) {
	Page<Categoria> lista = servicioCategoria.findAllPaginated(page);
	model.addAttribute("categorias", lista);
	return "categorias/lista-categorias";
	}

	@GetMapping("/crear")
	public String crear(Categoria categoria) {
		return "categorias/formulario-categoria";
	}

	@PostMapping("/insertar")
	public String insertar(Categoria categoria,
			BindingResult result, RedirectAttributes attributes) {
		
		if (result.hasErrors()) {
			return "categorias/formulario-categoria";
		}
		
		servicioCategoria.insert(categoria);
		attributes.addFlashAttribute("message", "Categoría creada correctamente");
		return "redirect:/categorias/index";
	}
	
	@GetMapping("/editar/{id}")
	public String update(@PathVariable("id") int id, Model model) {
		Categoria categoria = servicioCategoria.findById(id);
		model.addAttribute("categoria", categoria);
		return "categorias/formulario-categoria";
		
	}
	
	@GetMapping("/eliminar/{id}")
	public String delete(@PathVariable("id") int id, RedirectAttributes attributes) { 
		try {
			servicioCategoria.delete(id);
			attributes.addFlashAttribute("message", "La categoría se ha eliminado correctamente");
		}
		catch (Exception e) {
			attributes.addFlashAttribute("message", 
					"La categoría no se ha podido eliminar, esta categoría está asignada a una o varias ofertas de empleo.");
		}
		return "redirect:/categorias/index-categorias-paginado";	
	}
	
	@GetMapping("categoria/{id}")
	public String mostrarCategoria(@PathVariable("id") int id, Model model) {
		Categoria categoria = servicioCategoria.findById(id);
		model.addAttribute("categoria", categoria);
		return "categorias/detalle-categoria";
	}
	
}

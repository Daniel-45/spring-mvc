package com.dam.controladores;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dam.modelos.Vacante;
import com.dam.servicios.IServicioCategoria;
import com.dam.servicios.IServicioVacante;
import com.dam.utilidades.Utilidades;

@Controller
@RequestMapping("vacantes")
public class ControladorVacante {
	
	@Value("${ticempleo.ruta.imagenes}")
	private String ruta;

	@Autowired
	private IServicioVacante servicioVacante;

	@Autowired
	private IServicioCategoria servicioCategoria;
	
	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Vacante> lista = servicioVacante.findAll();
		model.addAttribute("vacantes", lista);
		return "vacantes/lista-vacantes";
	}
	
	@GetMapping(value = "/index-vacantes-paginado")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Vacante> lista = servicioVacante.findAllPaginated(page);
		model.addAttribute("vacantes", lista);
		return "vacantes/lista-vacantes";
	}

	@GetMapping("/crear")
	public String crear(Vacante vacante, Model model) {
		return "vacantes/formulario-vacante";
	}

	@PostMapping("/insertar")
	public String insertar(Vacante vacante, BindingResult result, RedirectAttributes attributes,
			@RequestParam("file") MultipartFile file) {

		if (result.hasErrors()) {
			return "vacantes/formulario-vacante";
		}

		if (!file.isEmpty()) {
			String nombreImagen = Utilidades.guardarArchivo(file, ruta);
			if (nombreImagen != null) {
				vacante.setImagen(nombreImagen);
			}
		}

		servicioVacante.insert(vacante);
		System.out.println("Vacante: " + vacante);
		attributes.addFlashAttribute("message", "Oferta de empleo creada correctamente");
		return "redirect:/vacantes/index";
	}
	
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") int id, Model model) {
		Vacante vacante = servicioVacante.findById(id);
		model.addAttribute("vacante", vacante);
		return "vacantes/formulario-vacante";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable("id") int id, Model model, RedirectAttributes attributes) {
		servicioVacante.delete(id);
		attributes.addFlashAttribute("message", "Oferta de empleo eliminada correctamente");
		return "redirect:/vacantes/index";

	}

	@GetMapping("/vacante/{id}")
	public String mostrarDetalleVacante(@PathVariable("id") int id, Model model) {
		Vacante vacante = servicioVacante.findById(id);
		model.addAttribute("vacante", vacante);
		return "vacantes/detalle-vacante";
	}
	
	// Añadir datos al modelo que son comunes para todo el controlador
	@ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("categorias", servicioCategoria.findAll());
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}

			@Override
			public String getAsText() throws IllegalArgumentException {
				return DateTimeFormatter.ofPattern("dd-MM-yyyy").format((LocalDate) getValue());
			}
		});
	}

}

package com.dam.controladores;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dam.modelos.Solicitud;
import com.dam.modelos.Usuario;
import com.dam.modelos.Vacante;
import com.dam.servicios.IServicioSolicitud;
import com.dam.servicios.IServicioUsuario;
import com.dam.servicios.IServicioVacante;
import com.dam.utilidades.Utilidades;

@Controller
@RequestMapping("/solicitudes")
public class ControladorSolicitud {
	@Value("${ticempleo.ruta.cv}")
	private String ruta;

	@Autowired
	private IServicioSolicitud servicioSolicitud;
	
	@Autowired
	private IServicioVacante servicioVacante;

	@Autowired
	private IServicioUsuario servicioUsuario;

	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Solicitud> lista = servicioSolicitud.findAll();
		model.addAttribute("solicitudes", lista);
		return "solicitudes/lista-solicitudes";
	}
	
	@GetMapping("/index-solicitudes-paginado")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Solicitud> lista = servicioSolicitud.findAllPaginated(page);
		model.addAttribute("solicitudes", lista);
		return "solicitudes/lista-Solicitudes";
	}
	
	@GetMapping("/solicitud/crear/{idVacante}")
	public String crear(Solicitud solicitud, @PathVariable Integer idVacante, Model model) {
		// Traemos los detalles de la Vacante seleccionada para despues mostrarla en la vista
		Vacante vacante = servicioVacante.findById(idVacante);
		model.addAttribute("vacante", vacante);
		return "solicitudes/formulario-solicitudes";
	}
	
	@PostMapping("/solicitud//insertar")
	public String insertar(Solicitud solicitud, 
			BindingResult result, Model model, HttpSession session,
			@RequestParam("archivoCV") MultipartFile multiPart, 
			RedirectAttributes attributes, Authentication authentication) {	
		
		// Recuperar el username que inicio sesión
		String username = authentication.getName();
		
		if (result.hasErrors()){
			
			System.out.println("Existieron errores");
			return "solicitudes/formulario-solicitudes";
		}	
		
		if (!multiPart.isEmpty()) {
			String nombreArchivo = Utilidades.guardarArchivo(multiPart, ruta);
			 // El archivo (CV) si se ha podido subir
			if (nombreArchivo != null) {			
				solicitud.setArchivo(nombreArchivo); // Asignamos el nombre del archivo
			}	
		}

		// Buscar el objeto Usuario en Base de Datos	
		Usuario usuario = servicioUsuario.findByUserName(username);		
		
		solicitud.setUsuario(usuario); // Referenciamos la solicitud con el usuario 
		solicitud.setFecha(LocalDate.now());
		// Guardar el objeto solicitud en la Base de Datos
		servicioSolicitud.insert(solicitud);
		attributes.addFlashAttribute("message", "Gracias por enviar tú curriculum!");
		
		return "redirect:/";		
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable("id") int idSolicitud, 
			RedirectAttributes attributes) {
		
		// Eliminamos la solicitud.
		servicioSolicitud.delete(idSolicitud);
			
		attributes.addFlashAttribute("message", "La solicitud se ha eliminado correctamente");
		//return "redirect:/solicitudes/index";
		return "redirect:/solicitudes/index-solicitudes-paginado";
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException{
				setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}

			@Override
			public String getAsText() throws IllegalArgumentException {
				return DateTimeFormatter.ofPattern("dd-MM-yyyy").format((LocalDate) getValue());
			}
		});
	}
}

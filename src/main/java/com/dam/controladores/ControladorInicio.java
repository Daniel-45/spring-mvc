package com.dam.controladores;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dam.modelos.Perfil;
import com.dam.modelos.Usuario;
import com.dam.modelos.Vacante;
import com.dam.servicios.IServicioCategoria;
import com.dam.servicios.IServicioUsuario;
import com.dam.servicios.IServicioVacante;

@Controller
@RequestMapping("/")
public class ControladorInicio {
	
	@Autowired
	private IServicioCategoria servicioCategoria;
	
	@Autowired
	private IServicioVacante servicioVacante;
	
	@Autowired
	private IServicioUsuario servicioUsuario;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String inicio(Model model) {
		return "index";
	}
	
	@GetMapping("/index")
	public String mostrarIndex(Authentication auth, HttpSession session) {
		String username = auth.getName();
		if (session.getAttribute("usuario") == null) {			
			Usuario usuario = servicioUsuario.findByUserName(username);
			// No almacenar la contraseña en la sesión
			usuario.setPassword(null);
			System.out.println("Usuario: " + usuario);
			session.setAttribute("usuario", usuario);
		}
		return "redirect:/";
	}
	
	@GetMapping("/signup")
	public String registrarse(Usuario usuario) {
		return "/usuarios/formulario-registro";
	}
	
	/* Registrar usuario */
	@PostMapping("/signup")
	public String guardarRegistro(Usuario usuario, RedirectAttributes attributes) {
		String plainTextPassword = usuario.getPassword(); // Password en texto plano
		String encryptedPassword = passwordEncoder.encode(plainTextPassword);
		usuario.setPassword(encryptedPassword);
		usuario.setEstatus(1); // Activado por defecto
		usuario.setFechaRegistro(LocalDate.now());
		Perfil perfil = new Perfil();
		perfil.setId(3); // ROL por defecto 'Usuario'
		usuario.incluirPerfil(perfil);
		servicioUsuario.insert(usuario);
		attributes.addFlashAttribute("message", "Registro guardado correctamente.");
		return "redirect:/index";
	}
	
	/* Mapear URL al formulario de login personalizado */
	@GetMapping("/login")
	public String login() {
		return "formulario-login";
	}

	/* Cerrar sesión */
	// El objeto request es necesario para que Spring obtenga la sesión actual para invalidar
	@GetMapping("/logout")
	public String logout(HttpServletRequest request){
		// Implementación de Spring Security encargada de destruir la sesión
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/login";
	}
	
	@GetMapping("/search")
	public String buscar(@ModelAttribute("search") Vacante vacante, Model model) {
		System.out.println("Buscando: " + vacante);
		ExampleMatcher matcher = ExampleMatcher
				// WHERE descripcion LIKE '%?%'
				.matching().withMatcher("descripcion", ExampleMatcher.GenericPropertyMatchers.contains());
		Example<Vacante> example = Example.of(vacante, matcher);
		List<Vacante> lista = servicioVacante.findByExample(example);
		model.addAttribute("vacantes", lista);
		return "index";
	}
	
	/* Encriptar contraseñas para ver la contraseña encriptada en el navegador */
	// Introducir en el navegador localhost:9090/bcrypt/texto-a-encriptar
	// Ejemplo: localhost:9090/bcrypt/administrador
	@GetMapping("/bcrypt/{texto}")
	@ResponseBody
	public String encriptar(@PathVariable("texto") String texto) {
		return texto + " Encriptado con Bcrypt: " + passwordEncoder.encode(texto);
	}
	
	/**
	 * Initbinder si detecta Strings vacíos en el Data Binding los setea a null
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
	
	/**
	 * ModelAttribute permite añadir al modelo todos los atributos
	 * que se quieran añadir y están disponibles para todos los Métodos
	 * declarados en esta controlador.
	 * @param model
	 */

	@ModelAttribute
	public void setGenericos(Model model) {
		Vacante buscarVacante = new Vacante();
		buscarVacante.reset();
		model.addAttribute("vacantes", servicioVacante.findAllNoted());
		model.addAttribute("categorias", servicioCategoria.findAll());
		model.addAttribute("search", buscarVacante);
	}

}

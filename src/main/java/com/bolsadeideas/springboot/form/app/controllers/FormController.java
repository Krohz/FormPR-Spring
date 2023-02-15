package com.bolsadeideas.springboot.form.app.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.springboot.form.app.editors.NombreMayusculaEditor;
import com.bolsadeideas.springboot.form.app.editors.PaisPropertyEditor;
import com.bolsadeideas.springboot.form.app.editors.RolesEditor;
import com.bolsadeideas.springboot.form.app.models.domain.Pais;
import com.bolsadeideas.springboot.form.app.models.domain.Role;
import com.bolsadeideas.springboot.form.app.models.domain.Usuario;
import com.bolsadeideas.springboot.form.app.services.PaisService;
import com.bolsadeideas.springboot.form.app.services.RoleService;
import com.bolsadeideas.springboot.form.app.validation.UsuarioValidator;

@Controller
@SessionAttributes("usuario")
public class FormController {

	@Autowired
	private UsuarioValidator validator;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PaisPropertyEditor paisEditor;
	
	@Autowired
	private RolesEditor roleEditor;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(validator);

		binder.registerCustomEditor(String.class, new NombreMayusculaEditor());
		
		binder.registerCustomEditor(Pais.class, "pais", paisEditor);
		binder.registerCustomEditor(Role.class, "roles", roleEditor);
	}
	
	@ModelAttribute("listaRoles")
	public List<Role> listaRoles(){
		return this.roleService.listar();
	}

	@ModelAttribute("listaPaises")
	public List<Pais> listaPaises() {
		return paisService.listar();
	}
	
	@ModelAttribute("listadoRolesString")
	public List<String> listaRolesString(){
		List<String> roles = new ArrayList<>();
		roles.add("ROLE_ADMIN");
		roles.add("ROLE_USER");
		roles.add("ROLE_MODERATOR");
		return roles;
	}
	
	@ModelAttribute("listadoRolesMap")
	public Map<String, String> listadoRolesMap() {
		Map<String, String> roles = new HashMap<String, String>();
		roles.put("ROLE_ADMIN", "Administrador");
		roles.put("ROLE_USER", "Usuario");
		roles.put("ROLE_MODERATOR", "moderador");
		return roles;
	}

	@ModelAttribute("paises")
	public List<String> paises() {
		return Arrays.asList("Panamá", "Chile", "Brasil", "Colombia");
	}

	@ModelAttribute("paisesMap")
	public Map<String, String> paisesMap() {
		Map<String, String> paises = new HashMap<String, String>();
		paises.put("PA", "Panamá");
		paises.put("ES", "España");
		paises.put("CL", "Chile");
		paises.put("BR", "Brasil");
		paises.put("CO", "Colombia");
		return paises;
	}

	@GetMapping("/form")
	public String form(Model model) {
		Usuario usuario = new Usuario();
		usuario.setNombre("Ej: Cristian");
		usuario.setApellido("Ej: Kal");
		usuario.setId("16.123.123-J");
		model.addAttribute("titulo", "Formulario - Usuarios");
		model.addAttribute("usuario", usuario);
		return "form";
	}

	/*
	 * @PostMapping("/form")
	 * 
	 * public String procesar(Model model,
	 * 
	 * @RequestParam String username,
	 * 
	 * @RequestParam String password,
	 * 
	 * @RequestParam String email) {
	 * 
	 * Usuario usuario = new Usuario(); usuario.setUsername(username);
	 * usuario.setEmail(email); usuario.setPassword(password);
	 * model.addAttribute("titulo", "Formulario - Resultado");
	 * model.addAttribute("usuario", usuario); return "resultado"; }
	 */

	// Version pro - Hace lo mismo que el codigo de arriba -
	// Importante tener get-set en la clase usuario
	@PostMapping("/form")
	public String procesar(@Valid Usuario usuario, BindingResult result, Model model, SessionStatus status) {

		// validator.validate(usuario, result);

		model.addAttribute("titulo", "Formulario - Resultado");
		if (result.hasErrors()) {
			/*
			 * Map<String, String> errores = new HashMap<>();
			 * result.getFieldErrors().forEach(err ->{ errores.put(err.getField(),
			 * "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()
			 * )); }); model.addAttribute("error", errores);
			 */
			return "form";
		}
		model.addAttribute("usuario", usuario);
		status.setComplete();

		return "resultado";
	}
}

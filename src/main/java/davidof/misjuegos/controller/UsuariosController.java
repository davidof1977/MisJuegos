package davidof.misjuegos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import davidof.misjuegos.repository.entity.Usuario;
import davidof.misjuegos.service.UsuarioService;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api")
@RestController
public class UsuariosController {
	
		
		@Autowired
		private UsuarioService usuarioService;

		
		@PostMapping(path = "/registro", consumes = "application/json")
		public void guardar(@RequestBody Usuario usuario) {
			usuarioService.guardar(usuario);
		}
		
			
		@GetMapping("/login/{nombre}")
		public Boolean validarUsuario(@PathVariable String nombre, @RequestHeader("password") String password) {
			List<Usuario> usuario = usuarioService.obtenerUsuario(nombre);
			if (usuario != null) {
				return usuario.stream().anyMatch(u -> u.getPassword().equalsIgnoreCase(password));
			}else {

		        throw new ResponseStatusException(
				          HttpStatus.NOT_FOUND, "El usuario o contrasenia incorrectos");
			}			
		}	
}

package davidof.misjuegos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import davidof.misjuegos.repository.entity.Usuario;
import davidof.misjuegos.service.UsuarioService;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("")
@RestController
public class UsuariosController {
	
		
		@Autowired
		private UsuarioService usuarioService;

		//private BCryptPasswordEncoder bCryptPasswordEncoder;

		@PostMapping(path = "/registro", consumes = "application/json")
		public void guardar(@RequestBody Usuario usuario) {
			//bCryptPasswordEncoder = new BCryptPasswordEncoder();
			//usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
			usuarioService.guardar(usuario);
		}
		
			
		@PostMapping(path = "/login", consumes = "application/json")
		public Boolean validarUsuario(@RequestBody Usuario usuario) {
			List<Usuario> usuarios = usuarioService.obtenerUsuario(usuario.getNombre());
			//bCryptPasswordEncoder = new BCryptPasswordEncoder();
			if (usuario != null) {
				//return usuarios.stream().anyMatch(u -> u.getPassword().equalsIgnoreCase(bCryptPasswordEncoder.encode(usuario.getPassword())));
				return usuarios.stream().anyMatch(u -> u.getPassword().equalsIgnoreCase(usuario.getPassword()));
			}else {

		        throw new ResponseStatusException(
				          HttpStatus.NOT_FOUND, "El usuario o contrasenia incorrectos");
			}			
		}	
}

package br.com.massenan.gestaodecontratos.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.massenan.gestaodecontratos.domain.PerfilEnum;
import br.com.massenan.gestaodecontratos.domain.Usuario;
import br.com.massenan.gestaodecontratos.dto.UsuarioDto;
import br.com.massenan.gestaodecontratos.service.UsuarioService;

@Controller
@RequestMapping("/api/usuarios")
public class UsuarioController {
	private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private UsuarioService service;

	@GetMapping("/listar")
	public ResponseEntity<?> findAll() {
		try {
			return ResponseEntity.ok().body(UsuarioDto.parse(service.findAll()));
		} catch (Exception ex) {
			logger.error("[CARREGANDO-TODOS-OS-USUARIOS]", ex.fillInStackTrace());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/listarperfil")
	public ResponseEntity<?> listarPerfil() {
		try {
			return ResponseEntity.ok().body(getPerfis());
		} catch (Exception ex) {
			logger.error("[CARREGANDO-TODOS-OS-USUARIOS]", ex.fillInStackTrace());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<?> loadById(@PathVariable Long id) {
		return ResponseEntity.ok().body(UsuarioDto.parse(service.findById(id).get()));
	}

	@PostMapping("/criar")
	public ResponseEntity<?> create(@RequestBody UsuarioDto usuarioDto) {

		try {
			service.create(UsuarioDto.parse(usuarioDto));
			return new ResponseEntity<>(UsuarioDto.parse(service.findAll()), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/atualizar", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody UsuarioDto usuarioDto) {

		try {
			Usuario usr = service.findById(usuarioDto.getId()).get();
			usr.setUsername(usuarioDto.getUsername());
			service.update(usr);
			return ResponseEntity.ok().body(UsuarioDto.parse(service.findAll()));
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@PutMapping(value="/status/{status}/id/{id}")
	public ResponseEntity<?> setStatus(@PathVariable boolean status, @PathVariable Long id){
		
		try {
			service.updateStatus(id, status);
			return ResponseEntity.ok().body(UsuarioDto.parse(service.findAll()));
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value="/atualizarperfil/{perfil}/id/{id}")
	public ResponseEntity<?> setStatus(@PathVariable String perfil, @PathVariable Long id){
		
		try {
			PerfilEnum oPerfil = PerfilEnum.fromText(perfil);
			service.updatePerfil(id, oPerfil);
			return ResponseEntity.ok().body(UsuarioDto.parse(service.findAll()));
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public List<String> getPerfis(){
		List<String>list = new ArrayList<String>();
		
		for(PerfilEnum p : PerfilEnum.values()) {
			list.add(p.getTipo());
		}
		return list;
	}
}

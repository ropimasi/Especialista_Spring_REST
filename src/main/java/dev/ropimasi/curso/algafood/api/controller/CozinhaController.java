package dev.ropimasi.curso.algafood.api.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import dev.ropimasi.curso.algafood.domain.model.Cozinha;
import dev.ropimasi.curso.algafood.domain.repository.CozinhaRepository;
import dev.ropimasi.curso.algafood.domain.service.CozinhaCadastroService;

/* teste git  */




@RestController
@RequestMapping(value = "/cozinhas")//, produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private CozinhaCadastroService cozinhaCadastroService;



	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Cozinha> listar() {
		return cozinhaRepository.findAll();

	}



	//	@ResponseStatus(value= HttpStatus.OK)
	@GetMapping(value = "/{cozinhaId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
		Optional<Cozinha> cozinhaOpt = cozinhaRepository.findById(cozinhaId);

		//		return ResponseEntity.status(HttpStatus.OK).body(cozinha);
		//		return ResponseEntity.ok(cozinha);

		//		HttpHeaders headers = new HttpHeaders();
		//		headers.add(HttpHeaders.LOCATION, "http://localhost:8080/cozinhas");
		//		return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();

		if (cozinhaOpt.isPresent()) {
			return ResponseEntity.ok(cozinhaOpt.get());
		}
		//		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.notFound().build();
	}



	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		return cozinhaCadastroService.salvar(cozinha);
	}



	@PutMapping(value = "/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha) {
		Optional<Cozinha> cozinhaPersistidaOpt = cozinhaRepository.findById(cozinhaId);

		if (cozinhaPersistidaOpt.isPresent()) {
			// BeanUtils.copyProperties(cozinha, cozinhaPersistida, "id");
			cozinha.setId(cozinhaId);
			cozinha = cozinhaCadastroService.salvar(cozinha);
			return ResponseEntity.ok(cozinha);
		}

		return ResponseEntity.notFound().build();
	}

	//	@DeleteMapping(value = "/{cozinhaId}")
	//	public ResponseEntity<?> remover(@PathVariable Long cozinhaId) {
	//		try {
	//			cozinhaCadastroService.excluir(cozinhaId);
	//			return ResponseEntity.noContent().build();
	//
	//		} catch (EntidadeNaoEncontradaException e) {
	//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	//
	//		} catch (EntidadeEmUsoException e) {
	//			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	//		}
	//	}



	@DeleteMapping(value = "/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cozinhaCadastroService.excluir(cozinhaId);
	}

}

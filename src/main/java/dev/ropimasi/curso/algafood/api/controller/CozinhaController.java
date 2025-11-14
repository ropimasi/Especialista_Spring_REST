package dev.ropimasi.curso.algafood.api.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
	public Cozinha buscar(@PathVariable Long cozinhaId) {
		return cozinhaCadastroService.buscarOuFalhar(cozinhaId);
	}



	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody @Valid Cozinha cozinha) {
		return cozinhaCadastroService.salvar(cozinha);
	}



	@PutMapping(value = "/{cozinhaId}")
	//@ResponseStatus(HttpStatus.OK) já é padrão.
	public Cozinha atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid Cozinha cozinha) {
		Cozinha cozinhaPersistida = cozinhaCadastroService.buscarOuFalhar(cozinhaId);
		BeanUtils.copyProperties(cozinha, cozinhaPersistida, "id");
		return cozinhaCadastroService.salvar(cozinhaPersistida);
	}



	@DeleteMapping(value = "/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cozinhaCadastroService.excluir(cozinhaId);
	}

}

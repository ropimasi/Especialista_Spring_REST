package dev.ropimasi.curso.algafood.api.controller;

import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import dev.ropimasi.curso.algafood.domain.model.Estado;
import dev.ropimasi.curso.algafood.domain.repository.EstadoRepository;
import dev.ropimasi.curso.algafood.domain.service.EstadoCadastroService;




@RestController
@RequestMapping("/estados")
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private EstadoCadastroService estadoCadastroService;



	@GetMapping
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}



	@GetMapping("/{estadoId}")
	public Estado buscar(@PathVariable Long estadoId) {
		return estadoCadastroService.buscarOuFalhar(estadoId);
	}



	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Estado adicionar(@RequestBody Estado estado) {
		return estadoCadastroService.salvar(estado);
	}



	@PutMapping("/{estadoId}")
	//@ResponseStatus(HttpStatus.OK) já é padrão.
	public Estado atualizar(@PathVariable Long estadoId, @RequestBody Estado estado) {
		Estado estadoPersistido = estadoCadastroService.buscarOuFalhar(estadoId);

		BeanUtils.copyProperties(estado, estadoPersistido, "id");

		return estadoCadastroService.salvar(estadoPersistido);
	}



	@DeleteMapping(value = "/{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId) {
		estadoCadastroService.excluir(estadoId);
	}

}

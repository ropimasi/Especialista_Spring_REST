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
import dev.ropimasi.curso.algafood.domain.exception.EntidadeNaoEncontradaException;
import dev.ropimasi.curso.algafood.domain.exception.NegocioException;
import dev.ropimasi.curso.algafood.domain.model.Cidade;
import dev.ropimasi.curso.algafood.domain.repository.CidadeRepository;
import dev.ropimasi.curso.algafood.domain.service.CidadeCadastroService;




@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CidadeCadastroService cidadeCadastroService;



	@GetMapping
	public List<Cidade> listar() {
		return cidadeRepository.findAll();
	}



	@GetMapping(value = "{cidadeId}")
	public Cidade buscar(@PathVariable Long cidadeId) {
		return cidadeCadastroService.buscarOuFalhar(cidadeId);
	}



	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cidade adicionar(@RequestBody Cidade cidade) {
		try {
			return cidadeCadastroService.salvar(cidade);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}



	@PutMapping(value = "/{cidadeId}")
	public Cidade atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {
		Cidade cidadePersistida = cidadeCadastroService.buscarOuFalhar(cidadeId);
		BeanUtils.copyProperties(cidade, cidadePersistida, "id");
		try {
			return cidadeCadastroService.salvar(cidadePersistida);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());

		}
	}



	@DeleteMapping(value = "/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {
		cidadeCadastroService.excluir(cidadeId);
	}

}

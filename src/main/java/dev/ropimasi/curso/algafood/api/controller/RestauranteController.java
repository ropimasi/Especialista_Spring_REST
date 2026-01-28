package dev.ropimasi.curso.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ropimasi.curso.algafood.core.validation.Groups;
import dev.ropimasi.curso.algafood.domain.exception.CozinhaNaoEncontradaException;
import dev.ropimasi.curso.algafood.domain.exception.EntidadeNaoEncontradaException;
import dev.ropimasi.curso.algafood.domain.exception.NegocioException;
import dev.ropimasi.curso.algafood.domain.model.Restaurante;
import dev.ropimasi.curso.algafood.domain.repository.RestauranteRepository;
import dev.ropimasi.curso.algafood.domain.service.RestauranteCadastroService;




@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private RestauranteCadastroService restauranteCadastroService;



	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.findAll();
	}



	@GetMapping(value = "{restauranteId}")
	public Restaurante buscar(@PathVariable Long restauranteId) {
		return restauranteCadastroService.buscarOuFalhar(restauranteId);
	}



	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Restaurante adicionar(
			@RequestBody @Valid Restaurante restaurante) {
		try {
			return restauranteCadastroService.salvar(restaurante);
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}



	@PutMapping(value = "/{restauranteId}")
	public Restaurante atualizar(@PathVariable Long restauranteId, @RequestBody @Valid Restaurante restaurante) {
		Restaurante restaurantePersistido = restauranteCadastroService.buscarOuFalhar(restauranteId);
		BeanUtils.copyProperties(restaurante, restaurantePersistido, "id", "formasPagamento", "endereco",
				"dataCadastro", "produtos");
		try {
			return restauranteCadastroService.salvar(restaurantePersistido);
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}



	@PatchMapping(value = "/{restauranteId}")
	public Restaurante atualizarParcial(@PathVariable Long restauranteId,
			@RequestBody Map<String, Object> campos,
			HttpServletRequest request) {
		Restaurante restauranteAtual = restauranteCadastroService.buscarOuFalhar(restauranteId);

		merge(campos, restauranteAtual, request);

		return atualizar(restauranteId, restauranteAtual);
	}



	// Para o atualizarParcial().
	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		try {
			ObjectMapper objectMaapper = new ObjectMapper();
			objectMaapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMaapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		
			Restaurante restauranteOrigem = objectMaapper.convertValue(dadosOrigem, Restaurante.class);
		
			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
		
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
		
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}



	@DeleteMapping(value = "/{restauranteId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long restauranteId) {
		restauranteCadastroService.excluir(restauranteId);
	}

}

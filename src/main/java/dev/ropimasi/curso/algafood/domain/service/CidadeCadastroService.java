package dev.ropimasi.curso.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import dev.ropimasi.curso.algafood.domain.exception.CidadeNaoEncontradaException;
import dev.ropimasi.curso.algafood.domain.exception.EntidadeEmUsoException;
import dev.ropimasi.curso.algafood.domain.model.Cidade;
import dev.ropimasi.curso.algafood.domain.model.Estado;
import dev.ropimasi.curso.algafood.domain.repository.CidadeRepository;




@Service
public class CidadeCadastroService {

	private static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser removida, pois está em uso.";

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoCadastroService estadoCadastroService;



	public Cidade buscarOuFalhar(Long cidadeId) {
		return cidadeRepository.findById(cidadeId).orElseThrow(() -> new CidadeNaoEncontradaException(cidadeId));
	}



	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		Estado estado = estadoCadastroService.buscarOuFalhar(estadoId);
		cidade.setEstado(estado);
		return cidadeRepository.save(cidade);
	}



	public void excluir(Long cidadeId) {
		try {
			cidadeRepository.deleteById(cidadeId);

		} catch (EmptyResultDataAccessException e) {
			throw new CidadeNaoEncontradaException(cidadeId);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_CIDADE_EM_USO, cidadeId));
		}
	}

}

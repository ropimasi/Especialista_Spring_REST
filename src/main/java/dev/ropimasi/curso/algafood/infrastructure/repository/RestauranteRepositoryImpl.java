package dev.ropimasi.curso.algafood.infrastructure.repository;

import static dev.ropimasi.curso.algafood.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static dev.ropimasi.curso.algafood.infrastructure.repository.spec.RestauranteSpecs.comNomeSemelhante;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import dev.ropimasi.curso.algafood.domain.model.Restaurante;
import dev.ropimasi.curso.algafood.domain.repository.RestauranteRepository;
import dev.ropimasi.curso.algafood.domain.repository.RestauranteRepositoryQueries;




@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries { // SDJ implementação customizada.

	@PersistenceContext
	private EntityManager manager;

	@Autowired
	@Lazy
	private RestauranteRepository restauranteRepository;



	@Override
	public List<Restaurante> porNomeTaxaFreteEntre(String nome, BigDecimal taxaFreteInicial,
			BigDecimal taxaFreteFinal) {
		var jpql = "from Restaurante where nome like :nome and taxaFrete between :taxaInicial and :taxaFinal";

		return manager.createQuery(jpql, Restaurante.class).setParameter("nome", "%" + nome + "%")
				.setParameter("taxaInicial", taxaFreteInicial).setParameter("taxaFinal", taxaFreteFinal)
				.getResultList();
	}

	// Consulta dinâmica com JPQL.
	/*@Override
	public List<Restaurante> consulta(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		var jpql = new StringBuilder();
		jpql.append("from Restaurante where 0=0 ");
	
		var parametros = new HashMap<String, Object>();
	
		if (StringUtils.hasLength(nome)) {
			jpql.append("and nome like :nome ");
			parametros.put("nome", "%" + nome + "%");
		}
	
		if (taxaFreteInicial != null) {
			jpql.append("and taxaFrete >= :taxaInicial ");
			parametros.put("taxaInicial", taxaFreteInicial);
		}
	
		if (taxaFreteFinal != null) {
			jpql.append("and taxaFrete <= :taxaFinal ");
			parametros.put("taxaFinal", taxaFreteFinal);
		}
	
		TypedQuery<Restaurante> query =  em.createQuery(jpql.toString(), Restaurante.class);
		
		parametros.forEach((chave, valor) -> query.setParameter(chave, valor));
		 
		return query.getResultList();
	}*/



	// Consulta dinâmica com Criteria Query
	@Override
	public List<Restaurante> consulta(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Restaurante> restauranteCriteriaQuery = criteriaBuilder.createQuery(Restaurante.class);
		Root<Restaurante> restauranteRoot = restauranteCriteriaQuery.from(Restaurante.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (StringUtils.hasText(nome)) {
			predicates.add(criteriaBuilder.like(restauranteRoot.get("nome"), "%" + nome + "%"));
		}

		if (taxaFreteInicial != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(restauranteRoot.get("taxaFrete"), taxaFreteInicial));

		}

		if (taxaFreteFinal != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(restauranteRoot.get("taxaFrete"), taxaFreteFinal));
		}

		restauranteCriteriaQuery.where(predicates.toArray(new Predicate[0]));

		TypedQuery<Restaurante> restauranteTypedQuery = manager.createQuery(restauranteCriteriaQuery);
		return restauranteTypedQuery.getResultList();
	}



	@Override
	public List<Restaurante> findComFreteGratis(String nome) {
		return restauranteRepository.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
	}

}

package dev.ropimasi.curso.algafood.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;




@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class Pedido {

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Usuario cliente;

	@ManyToOne
	@JoinColumn(name = "restaurante_id", nullable = false)
	private Restaurante restaurante;

	@Embedded
	private Endereco enderecoEntrega;

	@ManyToOne
	@JoinColumn(name = "forma_pagamento_id", nullable = false)
	private FormaPagamento formaPagamento;

	private BigDecimal subtotal;

	private BigDecimal taxaFrete;

	private BigDecimal valorTotal;

	@CreationTimestamp
	private LocalDateTime dataCriacao;

	private LocalDateTime dataConfirmacao;

	private LocalDateTime dataCancelamento;

	private LocalDateTime dataEntrega;

	@Column(nullable = false)
	private StatusPedido status;

}

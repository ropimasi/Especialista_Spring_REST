CREATE TABLE pedido (
	id BIGINT NOT NULL AUTO_INCREMENT,
	cliente_id BIGINT NOT NULL,
	restaurante_id BIGINT NOT NULL,
	
	endereco_cep VARCHAR(10) NOT NULL,
	endereco_logradouro VARCHAR(100) NOT NULL,
	endereco_numero VARCHAR(10) NOT NULL,
	endereco_complemento VARCHAR(60),
	endereco_bairro VARCHAR(60) NOT NULL,
	endereco_cidade_id BIGINT NOT NULL,
	
	forma_pagamento_id BIGINT NOT NULL,
	subtotal DECIMAL(10,2) NOT NULL DEFAULT 0,
	taxa_frete DECIMAL(10,2) NOT NULL DEFAULT 0,
	valor_total DECIMAL(10,2) NOT NULL DEFAULT 0,
	
	data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	data_confirmacao TIMESTAMP,
	data_cancelamento TIMESTAMP,
	data_entrega TIMESTAMP,
	
	status VARCHAR(10) NOT NULL,
	
	CONSTRAINT pk_pedido PRIMARY KEY (id),
	CONSTRAINT fk_pedido_usuario_cliente_id FOREIGN KEY (cliente_id) REFERENCES usuario(id),
	CONSTRAINT fk_pedido_restaurante_id FOREIGN KEY (restaurante_id) REFERENCES restaurante(id),
	CONSTRAINT fk_pedido_cidade_id FOREIGN KEY (endereco_cidade_id) REFERENCES cidade(id),
	CONSTRAINT fk_pedido_forma_pagamento_id FOREIGN KEY (forma_pagamento_id) REFERENCES forma_pagamento(id)
) engine=InnoDB default charset=utf8mb4;


CREATE TABLE item_pedido (
	id BIGINT NOT NULL AUTO_INCREMENT,
	pedido_id BIGINT NOT NULL,
	produto_id BIGINT NOT NULL,
	quantidade INT NOT NULL,
	preco_unitario DECIMAL(10,2) NOT NULL,
	preco_total DECIMAL(10,2) NOT NULL,
	observacao VARCHAR(255),
	
	CONSTRAINT pk_item_pedido PRIMARY KEY (id),
	CONSTRAINT fk_item_pedido_pedido_id FOREIGN KEY (pedido_id) REFERENCES pedido(id),
	CONSTRAINT fk_item_pedido_produto_id FOREIGN KEY (produto_id) REFERENCES produto(id),
	CONSTRAINT uq_item_pedido_pedido_id_produto_id UNIQUE (pedido_id, produto_id)
) engine=InnoDB default charset=utf8mb4;

package com.example.netuno.model

data class Pedido(
	val ds_status: String,
	val vl_total: Double,
	val ds_numero: String,
	val createdAt: String,
	val ds_cidade: String,
	val updatedAt: String,
	val ds_uf: String,
	val id: Int,
	val ds_cep: String,
	val vl_frete: Int,
	val ds_complemento: String,
	val clienteId: Int,
	val ds_endereco: String,
	val itens: List<PedidoItem>
)

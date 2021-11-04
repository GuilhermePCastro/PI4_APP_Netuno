package com.example.netuno.model

data class PedidoItem(
	val updatedAt: String,
	val vl_produto: Double,
	val qt_produto: Int,
	val createdAt: String,
	val id: Int,
	val produto_id: Int,
	val pedido_id: Int
)

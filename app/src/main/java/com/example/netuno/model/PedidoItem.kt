package com.example.netuno.model

data class PedidoItem(
	val updated_at: String,
	val vl_produto: Double,
	val qt_produto: Int,
	val created_at: String,
	val id: Int,
	val produto_id: Int,
	val pedido_id: Int
)

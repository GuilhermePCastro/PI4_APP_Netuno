package com.example.netuno.model

import java.time.DateTimeException

data class Produto(
	val id: Int,
	val ds_foto: String,
	val tg_destaque: Int,
	val vl_produto: Double,
	val createdAt: String,
	val categoriaId: Int,
	val deletedAt: Any,
	val ds_descricao: String,
	val ds_nome: String,
	val updatedAt: Any,
	val ds_dimensoes: String,
	val qt_estoque: Int,
	val ds_peso: String,
	val marcaId: Int,
	val ds_material: String,
	val ds_linkfoto: String
)

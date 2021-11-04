package com.example.netuno.model

data class Carrinho(
	val quantidade: Int,
	val valor: Double,
	val itens: List<CarrinhoItem>
)

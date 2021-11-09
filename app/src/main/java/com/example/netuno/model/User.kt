package com.example.netuno.model

data class User(
	var email: String,
	val name: String,
	val updated_at: String,
	val created_at: String,
	val id: Int,
	val email_verified_at: Any,
	var device: String,
	var password: String,
	var token: String
)

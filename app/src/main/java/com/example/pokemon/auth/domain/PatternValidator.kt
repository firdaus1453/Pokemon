package com.example.pokemon.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}
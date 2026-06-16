package com.vvc.edge.gatekeeper.presentation.auth

/**
 * Representa los estados finitos y atómicos por los que pasa la pantalla de autenticación.
 * Evita mutaciones inválidas de la UI en Jetpack Compose.
 */
sealed class AuthState {
    object Idle : AuthState()
    object ProcessingCamera : AuthState()
    object AnalyzingFace : AuthState()
    data class Success(val token: String) : AuthState()
    data class Error(val errorMessage: String, val remainingAttempts: Int) : AuthState()
}


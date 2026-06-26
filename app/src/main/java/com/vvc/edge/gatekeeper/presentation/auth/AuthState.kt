package com.vvc.edge.gatekeeper.presentation.auth

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    class Error(val message: String) : AuthState()
}

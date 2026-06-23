package com.vvc.edge.gatekeeper.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvc.edge.gatekeeper.domain.usecase.VerifyFaceVectorUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val verifyFaceVectorUseCase: VerifyFaceVectorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)
    val uiState: StateFlow<AuthState> = _uiState

    fun verifyIdentity(capturedVector: FloatArray, patternVector: FloatArray) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            try {
                val isMatch = verifyFaceVectorUseCase.execute(capturedVector, patternVector)
                if (isMatch) {
                    _uiState.value = AuthState.Success
                } else {
                    _uiState.value = AuthState.Error("Access Denied: Identity mismatch")
                }
            } catch (e: Exception) {
                _uiState.value = AuthState.Error("System Error: ${e.localizedMessage}")
            }
        }
    }
}

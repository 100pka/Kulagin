package com.stopkaaaa.develoverslife.data

sealed class LoadingState {
    data class Success(val isFirstMem: Boolean) : LoadingState()
    data class Error(val description: String) : LoadingState()
    object Loading : LoadingState()
}
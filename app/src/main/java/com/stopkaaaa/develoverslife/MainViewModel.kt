package com.stopkaaaa.develoverslife

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopkaaaa.develoverslife.data.LoadingState
import com.stopkaaaa.develoverslife.data.Mem
import com.stopkaaaa.develoverslife.data.net.RetrofitClient
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {
    private val _mutableLoadingState = MutableLiveData(LoadingState.NEXT_MEM)
    private val _mutableCurrentMem = MutableLiveData<Mem>()

    val loadingState: LiveData<LoadingState> get() = _mutableLoadingState
    val currentMem: LiveData<Mem> get() = _mutableCurrentMem

    private val backStack = Stack<Mem>()
    private val forwardStack = Stack<Mem>()

    init {
        nextMem()
    }

    fun nextMem() {
        if (forwardStack.isEmpty()) {
            viewModelScope.launch {
                _mutableLoadingState.value = LoadingState.LOADING

                val result = RetrofitClient.getRandomMem()
                val mem = result.body()
                if (mem != null && !mem.gifUrl.isNullOrBlank()) {
                    backStack.add(mem)
                    _mutableCurrentMem.value = mem
                    if (backStack.size > 1) {
                        _mutableLoadingState.value = LoadingState.NEXT_MEM
                    } else {
                        _mutableLoadingState.value = LoadingState.FIRST_MEM
                    }
                } else {
                    _mutableLoadingState.value = LoadingState.ERROR
                    nextMem()
                }
            }
        } else {
            _mutableLoadingState.value = LoadingState.LOADING

            backStack.add(forwardStack.pop())

            val mem = backStack.lastElement()
            if (mem != null && !mem.gifUrl.isNullOrBlank()) {
                _mutableCurrentMem.value = mem
                _mutableLoadingState.value = LoadingState.NEXT_MEM
            } else {
                _mutableLoadingState.value = LoadingState.ERROR
            }
        }
    }

    fun previousMem() {
        if (backStack.size > 1) {
            _mutableLoadingState.value = LoadingState.LOADING

            forwardStack.add(backStack.pop())

            val mem = backStack.lastElement()
            if (mem != null && !mem.gifUrl.isNullOrBlank()) {
                _mutableCurrentMem.value = mem
                if (backStack.size > 1) {
                    _mutableLoadingState.value = LoadingState.NEXT_MEM
                } else {
                    _mutableLoadingState.value = LoadingState.FIRST_MEM
                }
            } else {
                _mutableLoadingState.value = LoadingState.ERROR
            }
        }
    }
}
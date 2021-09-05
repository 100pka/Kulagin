package com.stopkaaaa.develoverslife.memesfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopkaaaa.develoverslife.data.LoadingState
import com.stopkaaaa.develoverslife.data.Mem
import com.stopkaaaa.develoverslife.data.net.RetrofitClient
import java.util.*
import kotlinx.coroutines.launch

class MemesViewModel(private val tabTitle: TabTitles) : ViewModel() {
    private val _mutableLoadingState = MutableLiveData<LoadingState>(LoadingState.Loading)
    private val _mutableCurrentMem = MutableLiveData<Mem>()

    val loadingState: LiveData<LoadingState>
        get() = _mutableLoadingState
    val currentMem: LiveData<Mem>
        get() = _mutableCurrentMem

    private val backStack = Stack<Mem>()
    private val forwardStack = Stack<Mem>()
    private var lastLoadedPage = 0

    init {
        getMemes()
    }

    fun nextMem() {
        viewModelScope.launch {
            if (forwardStack.isEmpty()) {
                lastLoadedPage++
                getMemes()
            } else {
                _mutableLoadingState.value = LoadingState.Loading
                backStack.add(forwardStack.pop())
                val mem = backStack.peek()
                if (mem.gifUrl.isNotBlank()) {
                    _mutableCurrentMem.value = mem
                    _mutableLoadingState.value = LoadingState.Success(false)
                } else {
                    _mutableLoadingState.value = LoadingState.Error("Wrong GIFs Url")
                }
            }
        }
    }

    fun previousMem() {
        if (backStack.size > 1) {
            _mutableLoadingState.value = LoadingState.Loading
            forwardStack.add(backStack.pop())
            val mem = backStack.peek()
            if (mem.gifUrl.isNotBlank()) {
                _mutableCurrentMem.value = mem
                if (backStack.size > 1) {
                    _mutableLoadingState.value = LoadingState.Success(false)
                } else {
                    _mutableLoadingState.value = LoadingState.Success(true)
                }
            } else {
                _mutableLoadingState.value = LoadingState.Error("Wrong GIFs Url")
            }
        }
    }

    fun retryLoad() {
        getMemes()
    }

    private fun getMemes() {
        viewModelScope.launch {
            _mutableLoadingState.value = LoadingState.Loading
            val memesResult =
                when (tabTitle) {
                    TabTitles.LATEST -> {
                        RetrofitClient.getLatestMemes(lastLoadedPage)
                    }
                    TabTitles.TOP -> {
                        RetrofitClient.getTopMemes(lastLoadedPage)
                    }
                    TabTitles.HOT -> {
                        RetrofitClient.getHotMemes(lastLoadedPage)
                    }
                    TabTitles.RANDOM -> {
                        RetrofitClient.getRandomMem()
                    }
                }
            if (memesResult.isSuccess) {
                memesResult.getOrNull()?.let {
                    if (it.isNotEmpty()) {
                        val mem = it.first()
                        backStack.add(mem)
                        forwardStack.addAll(it.subList(1, it.size))
                        if (mem.gifUrl.isNotBlank()) {
                            _mutableCurrentMem.value = mem
                            if (backStack.size > 1) {
                                _mutableLoadingState.value = LoadingState.Success(false)
                            } else {
                                _mutableLoadingState.value = LoadingState.Success(true)
                            }
                        } else {
                            _mutableLoadingState.value = LoadingState.Error("Wrong GIFs Url")
                        }
                    } else {
                        _mutableLoadingState.value =
                            LoadingState.Error("Empty memes list")
                    }
                }
            } else {
                _mutableLoadingState.value =
                    LoadingState.Error(memesResult.exceptionOrNull()?.message ?: "Something went wrong")
            }
        }
    }
}

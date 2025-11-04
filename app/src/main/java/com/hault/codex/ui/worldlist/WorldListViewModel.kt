package com.hault.codex.ui.worldlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hault.codex.data.model.World
import com.hault.codex.data.repository.WorldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorldListViewModel @Inject constructor(
    private val worldRepository: WorldRepository
) : ViewModel() {

    val worlds: StateFlow<List<World>> = worldRepository.allWorlds
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteWorld(world: World) {
        viewModelScope.launch {
            worldRepository.delete(world)
        }
    }
}

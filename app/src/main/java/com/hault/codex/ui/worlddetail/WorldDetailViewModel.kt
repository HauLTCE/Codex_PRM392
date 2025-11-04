package com.hault.codex.ui.worlddetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hault.codex.data.model.World
import com.hault.codex.data.repository.WorldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorldDetailViewModel @Inject constructor(
    private val worldRepository: WorldRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _world = MutableStateFlow<World?>(null)
    val world: StateFlow<World?> = _world

    val worldId = savedStateHandle.get<Int>("worldId") ?: -1

    init {
        if (worldId != -1) {
            viewModelScope.launch {
                worldRepository.getWorldById(worldId)?.let {
                    _world.value = it
                }
            }
        }
    }

    fun deleteWorld(world: World) {
        viewModelScope.launch {
            worldRepository.delete(world)
        }
    }
}

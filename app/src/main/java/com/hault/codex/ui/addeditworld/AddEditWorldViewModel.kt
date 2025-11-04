package com.hault.codex.ui.addeditworld

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hault.codex.data.model.World
import com.hault.codex.data.repository.WorldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditWorldViewModel @Inject constructor(
    private val worldRepository: WorldRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _worldId = savedStateHandle.get<Int>("worldId") ?: -1

    val worldName = MutableStateFlow("")
    val worldDescription = MutableStateFlow("")

    init {
        if (_worldId != -1) {
            viewModelScope.launch {
                worldRepository.getWorldById(_worldId)?.let { world ->
                    worldName.value = world.name
                    worldDescription.value = world.description ?: ""
                }
            }
        }
    }

    fun saveWorld() {
        if (worldName.value.isNotBlank()) {
            viewModelScope.launch {
                if (_worldId != -1) {
                    val updatedWorld = World(id = _worldId, name = worldName.value, description = worldDescription.value)
                    worldRepository.update(updatedWorld)
                } else {
                    val newWorld = World(name = worldName.value, description = worldDescription.value)
                    worldRepository.insert(newWorld)
                }
            }
        }
    }
}

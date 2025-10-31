package com.hault.codex.ui.addeditworld

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
    private val worldRepository: WorldRepository
) : ViewModel() {

    val worldName = MutableStateFlow("")

    fun saveWorld() {
        if (worldName.value.isNotBlank()) {
            val newWorld = World(name = worldName.value, description = null)
            viewModelScope.launch {
                worldRepository.insert(newWorld)
            }
        }
    }
}

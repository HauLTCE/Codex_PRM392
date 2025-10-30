package com.username.codex.ui.addeditworld

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.username.codex.data.model.World
import com.username.codex.data.repository.WorldRepository
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

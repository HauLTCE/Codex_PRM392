package com.username.codex.ui.worldlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.username.codex.data.model.World
import com.username.codex.data.repository.WorldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WorldListViewModel @Inject constructor(
    worldRepository: WorldRepository
) : ViewModel() {

    val worlds: StateFlow<List<World>> = worldRepository.allWorlds
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

package com.hault.codex.ui.worlddashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hault.codex.data.model.Character
import com.hault.codex.data.model.Location
import com.hault.codex.data.repository.CharacterRepository
import com.hault.codex.data.repository.LocationRepository
import com.hault.codex.data.repository.WorldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.hault.codex.data.model.Event
import com.hault.codex.data.repository.EventRepository

@HiltViewModel
class WorldDashboardViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val worldRepository: WorldRepository,
    private val locationRepository: LocationRepository,
    private val eventRepository: EventRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val worldId: Int = savedStateHandle.get<Int>("worldId") ?: throw IllegalStateException("worldId not found in SavedStateHandle")

    private val _worldName = MutableStateFlow("Characters")
    val worldName: StateFlow<String> = _worldName.asStateFlow()

    init {
        viewModelScope.launch {
            val world = worldRepository.getWorldById(worldId)
            world?.let { _worldName.value = it.name }
        }
    }

    val characters: StateFlow<List<Character>> =
        characterRepository.getCharactersForWorld(worldId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val locations: StateFlow<List<Location>> =
        locationRepository.getLocationsForWorld(worldId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun deleteCharacter(character: Character) {
        viewModelScope.launch {
            characterRepository.delete(character)
        }
    }

    fun deleteLocation(location: Location) {
        viewModelScope.launch {
            locationRepository.delete(location)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.delete(event)
        }
    }
}

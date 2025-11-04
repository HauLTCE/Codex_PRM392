package com.hault.codex.ui.addeditcharacter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hault.codex.data.model.Character
import com.hault.codex.data.model.Location
import com.hault.codex.data.repository.CharacterRepository
import com.hault.codex.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val locationRepository: LocationRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val worldId: Int = savedStateHandle.get<Int>("worldId") ?: throw IllegalStateException("worldId not found in SavedStateHandle")

    val characterName = MutableStateFlow("")
    val characterBackstory = MutableStateFlow("")
    val homeLocationId = MutableStateFlow<Int?>(null)

    val locations: StateFlow<List<Location>> =
        locationRepository.getLocationsForWorld(worldId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val characterId: Int? = savedStateHandle.get<Int>("characterId")

    init {
        if (characterId != null) {
            viewModelScope.launch {
                characterRepository.getCharacter(characterId)?.let { character ->
                    characterName.value = character.name
                    characterBackstory.value = character.backstory ?: ""
                    homeLocationId.value = character.homeLocationId
                }
            }
        }
    }

    fun onCharacterNameChange(newName: String) {
        characterName.value = newName
    }

    fun onCharacterBackstoryChange(newBackstory: String) {
        characterBackstory.value = newBackstory
    }

    fun onHomeLocationChange(locationId: Int?) {
        homeLocationId.value = locationId
    }

    fun saveCharacter() {
        viewModelScope.launch {
            val character = Character(
                id = characterId ?: 0,
                name = characterName.value,
                backstory = characterBackstory.value,
                worldId = worldId,
                homeLocationId = homeLocationId.value
            )
            if (characterId == null) {
                characterRepository.insert(character)
            } else {
                characterRepository.update(character)
            }
        }
    }
}
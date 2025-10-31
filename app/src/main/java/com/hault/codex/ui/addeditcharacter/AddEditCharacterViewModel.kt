package com.hault.codex.ui.addeditcharacter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hault.codex.data.model.Character
import com.hault.codex.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _characterName = MutableStateFlow("")
    val characterName: StateFlow<String> = _characterName

    private val _characterBackstory = MutableStateFlow("")
    val characterBackstory: StateFlow<String> = _characterBackstory

    fun onCharacterNameChange(newName: String) {
        _characterName.value = newName
    }

    fun onCharacterBackstoryChange(newBackstory: String) {
        _characterBackstory.value = newBackstory
    }

    fun saveCharacter() {
        viewModelScope.launch {
            val worldId: Int = savedStateHandle.get<Int>("worldId") ?: throw IllegalStateException("worldId not found in SavedStateHandle")
            val character = Character(
                name = _characterName.value,
                backstory = _characterBackstory.value,
                worldId = worldId
            )
            characterRepository.insert(character)
        }
    }
}

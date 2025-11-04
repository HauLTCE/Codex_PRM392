package com.hault.codex.ui.addedit.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hault.codex.data.model.Event
import com.hault.codex.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _eventName = MutableStateFlow("")
    val eventName: StateFlow<String> = _eventName

    private val _eventDate = MutableStateFlow("")
    val eventDate: StateFlow<String> = _eventDate

    private val _eventDescription = MutableStateFlow("")
    val eventDescription: StateFlow<String> = _eventDescription

    private val worldId: Int = savedStateHandle.get<Int>("worldId")!!
    private val eventId: Int? = savedStateHandle.get<Int>("eventId")

    init {
        if (eventId != null) {
            viewModelScope.launch {
                eventRepository.getEvent(eventId)?.let { event ->
                    _eventName.value = event.name
                    _eventDate.value = event.date
                    _eventDescription.value = event.description ?: ""
                }
            }
        }
    }

    fun onEventNameChange(newName: String) {
        _eventName.value = newName
    }

    fun onEventDateChange(newDate: String) {
        _eventDate.value = newDate
    }

    fun onEventDescriptionChange(newDescription: String) {
        _eventDescription.value = newDescription
    }

    fun saveEvent() {
        viewModelScope.launch {
            val event = Event(
                id = eventId ?: 0,
                name = _eventName.value,
                date = _eventDate.value,
                description = _eventDescription.value,
                worldId = worldId
            )
            if (eventId == null) {
                eventRepository.insert(event)
            } else {
                eventRepository.update(event)
            }
        }
    }
}

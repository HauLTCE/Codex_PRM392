package com.hault.codex.ui.timeline

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hault.codex.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val worldId: Int = savedStateHandle.get<Int>("worldId")!!

    val events = eventRepository.getEventsForWorld(worldId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

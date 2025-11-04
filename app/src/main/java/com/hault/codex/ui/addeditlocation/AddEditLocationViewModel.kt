package com.hault.codex.ui.addeditlocation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hault.codex.data.model.Location
import com.hault.codex.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditLocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val worldId = savedStateHandle.get<Int>("worldId") ?: -1
    private val locationId = savedStateHandle.get<Int>("locationId") ?: -1

    val locationName = MutableStateFlow("")
    val locationDescription = MutableStateFlow("")

    init {
        if (locationId != -1) {
            viewModelScope.launch {
                locationRepository.getLocationById(locationId)?.let { location ->
                    locationName.value = location.name
                    locationDescription.value = location.description ?: ""
                }
            }
        }
    }

    fun saveLocation() {
        if (locationName.value.isNotBlank()) {
            viewModelScope.launch {
                if (locationId != -1) {
                    val updatedLocation = Location(id = locationId, worldId = worldId, name = locationName.value, description = locationDescription.value)
                    locationRepository.update(updatedLocation)
                } else {
                    val newLocation = Location(worldId = worldId, name = locationName.value, description = locationDescription.value)
                    locationRepository.insert(newLocation)
                }
            }
        }
    }
}

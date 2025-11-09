package com.hault.codex_java.ui.character;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.viewmodel.CharacterViewModel;
import com.hault.codex_java.viewmodel.LocationViewModel;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddCharacterFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";

    private CharacterViewModel characterViewModel;
    private LocationViewModel locationViewModel;
    private TextInputEditText editTextCharacterName;
    private TextInputEditText editTextCharacterBackstory;
    private Spinner spinnerHomeLocation;
    private List<Location> locationList = new ArrayList<>();
    private int worldId;
    private TextInputEditText editTextTags;
    private SwitchMaterial switchIsPinned;

    public static AddCharacterFragment newInstance(int worldId) {
        AddCharacterFragment fragment = new AddCharacterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WORLD_ID, worldId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            worldId = getArguments().getInt(ARG_WORLD_ID);
        }
        characterViewModel = new ViewModelProvider(this).get(CharacterViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_character, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextCharacterName = view.findViewById(R.id.editTextCharacterName);
        editTextCharacterBackstory = view.findViewById(R.id.editTextCharacterBackstory);
        spinnerHomeLocation = view.findViewById(R.id.spinnerHomeLocation);
        editTextTags = view.findViewById(R.id.editTextTags);
        switchIsPinned = view.findViewById(R.id.switchIsPinned);
        Button buttonSaveCharacter = view.findViewById(R.id.buttonSaveCharacter);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Add Character");

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        setupLocationSpinner();

        buttonSaveCharacter.setOnClickListener(v -> saveCharacter());
    }

    private void setupLocationSpinner() {
        locationViewModel.getLocationsForWorld(worldId).observe(getViewLifecycleOwner(), locations -> {
            this.locationList = locations;
            List<String> locationNames = new ArrayList<>();
            locationNames.add("No Location");
            locationNames.addAll(locations.stream().map(l -> l.name).collect(Collectors.toList()));

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, locationNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerHomeLocation.setAdapter(adapter);

        });
    }

    private void saveCharacter() {
        String name = editTextCharacterName.getText().toString().trim();
        String backstory = editTextCharacterBackstory.getText().toString().trim();
        String tags = editTextTags.getText().toString().trim();
        boolean isPinned = switchIsPinned.isChecked();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Character name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Integer locationId = null;
        int selectedPosition = spinnerHomeLocation.getSelectedItemPosition();
        if (selectedPosition > 0) {
            locationId = locationList.get(selectedPosition - 1).id;
        }

        Character newCharacter = new Character(name, backstory, worldId, locationId);
        newCharacter.tags = tags;
        newCharacter.isPinned = isPinned;
        // newCharacter.imageUri = ... (logic to be added)
        // newCharacter.colorHex = ... (logic to be added)

        characterViewModel.insert(newCharacter);

        getParentFragmentManager().popBackStack();
    }
}
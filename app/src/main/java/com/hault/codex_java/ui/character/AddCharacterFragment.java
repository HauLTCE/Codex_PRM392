package com.hault.codex_java.ui.character;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.ui.util.ColorPickerHelper;
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

    private ImageView imageViewHeader;
    private Button buttonSelectImage;
    private Uri selectedImageUri;
    private ColorPickerHelper colorPickerHelper;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

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

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                if (imageViewHeader != null) {
                    Glide.with(this).load(uri).centerCrop().into(imageViewHeader);
                }
            }
        });
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
        imageViewHeader = view.findViewById(R.id.imageViewHeader);
        buttonSelectImage = view.findViewById(R.id.buttonSelectImage);
        LinearLayout colorPickerContainer = view.findViewById(R.id.colorPickerContainer);
        colorPickerHelper = new ColorPickerHelper(colorPickerContainer, null);

        toolbar.setTitle("Add Character");
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        setupLocationSpinner();
        buttonSelectImage.setOnClickListener(v -> openImagePicker());
        buttonSaveCharacter.setOnClickListener(v -> saveCharacter());
    }

    private void openImagePicker() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
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
        if(selectedImageUri != null) {
            newCharacter.imageUri = selectedImageUri.toString();
        }
        newCharacter.colorHex = colorPickerHelper.getSelectedColorHex();

        characterViewModel.insert(newCharacter);

        getParentFragmentManager().popBackStack();
    }
}
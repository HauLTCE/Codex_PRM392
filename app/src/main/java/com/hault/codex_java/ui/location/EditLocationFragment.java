package com.hault.codex_java.ui.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.viewmodel.LocationViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditLocationFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private static final String ARG_LOCATION_ID = "location_id";

    private LocationViewModel locationViewModel;
    private TextInputEditText editTextLocationName;
    private TextInputEditText editTextLocationDescription;
    private TextInputEditText editTextTags;
    private SwitchMaterial switchIsPinned;
    private Location currentLocation;
    private int worldId;
    private int locationId;

    public static EditLocationFragment newInstance(int worldId, int locationId) {
        EditLocationFragment fragment = new EditLocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WORLD_ID, worldId);
        args.putInt(ARG_LOCATION_ID, locationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            worldId = getArguments().getInt(ARG_WORLD_ID);
            locationId = getArguments().getInt(ARG_LOCATION_ID);
        }
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextLocationName = view.findViewById(R.id.editTextLocationName);
        editTextLocationDescription = view.findViewById(R.id.editTextLocationDescription);
        editTextTags = view.findViewById(R.id.editTextTags);
        switchIsPinned = view.findViewById(R.id.switchIsPinned);
        Button buttonSaveLocation = view.findViewById(R.id.buttonSaveLocation);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Location");
        buttonSaveLocation.setText("Save Changes");

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        locationViewModel.getLocationById(locationId).observe(getViewLifecycleOwner(), location -> {
            if (location != null) {
                currentLocation = location;
                editTextLocationName.setText(currentLocation.name);
                editTextLocationDescription.setText(currentLocation.description);
                editTextTags.setText(currentLocation.tags);
                switchIsPinned.setChecked(currentLocation.isPinned);
            }
        });

        buttonSaveLocation.setOnClickListener(v -> saveLocation());
    }

    private void saveLocation() {
        if (currentLocation == null) return;

        String name = editTextLocationName.getText().toString().trim();
        String description = editTextLocationDescription.getText().toString().trim();
        String tags = editTextTags.getText().toString().trim();
        boolean isPinned = switchIsPinned.isChecked();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Location name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        currentLocation.name = name;
        currentLocation.description = description;
        currentLocation.tags = tags;
        currentLocation.isPinned = isPinned;
        locationViewModel.update(currentLocation);
        getParentFragmentManager().popBackStack();
    }
}
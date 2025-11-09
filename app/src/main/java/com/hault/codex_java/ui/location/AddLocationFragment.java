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
import com.google.android.material.textfield.TextInputEditText;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.viewmodel.LocationViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddLocationFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";

    private LocationViewModel locationViewModel;
    private TextInputEditText editTextLocationName;
    private TextInputEditText editTextLocationDescription;
    private int worldId;

    public static AddLocationFragment newInstance(int worldId) {
        AddLocationFragment fragment = new AddLocationFragment();
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
        Button buttonSaveLocation = view.findViewById(R.id.buttonSaveLocation);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Add Location");

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        buttonSaveLocation.setOnClickListener(v -> saveLocation());
    }

    private void saveLocation() {
        String name = editTextLocationName.getText().toString().trim();
        String description = editTextLocationDescription.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Location name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Location newLocation = new Location(name, description, worldId);
        locationViewModel.insert(newLocation);
        getParentFragmentManager().popBackStack();
    }
}
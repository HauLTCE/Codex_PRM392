package com.hault.codex_java.ui.location;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.ui.util.ColorPickerHelper;
import com.hault.codex_java.viewmodel.LocationViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddLocationFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";

    private LocationViewModel locationViewModel;
    private TextInputEditText editTextLocationName;
    private TextInputEditText editTextLocationDescription;
    private TextInputEditText editTextTags;
    private SwitchMaterial switchIsPinned;
    private int worldId;

    private ImageView imageViewHeader;
    private Button buttonSelectImage;
    private Uri selectedImageUri;
    private ColorPickerHelper colorPickerHelper;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

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
        imageViewHeader = view.findViewById(R.id.imageViewHeader);
        buttonSelectImage = view.findViewById(R.id.buttonSelectImage);
        LinearLayout colorPickerContainer = view.findViewById(R.id.colorPickerContainer);
        colorPickerHelper = new ColorPickerHelper(colorPickerContainer, null);

        toolbar.setTitle("Add Location");
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        buttonSelectImage.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        buttonSaveLocation.setOnClickListener(v -> saveLocation());
    }

    private void saveLocation() {
        String name = editTextLocationName.getText().toString().trim();
        String description = editTextLocationDescription.getText().toString().trim();
        String tags = editTextTags.getText().toString().trim();
        boolean isPinned = switchIsPinned.isChecked();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Location name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Location newLocation = new Location(name, description, worldId);
        newLocation.tags = tags;
        newLocation.isPinned = isPinned;
        if (selectedImageUri != null) {
            newLocation.imageUri = selectedImageUri.toString();
        }
        newLocation.colorHex = colorPickerHelper.getSelectedColorHex();

        locationViewModel.insert(newLocation);
        getParentFragmentManager().popBackStack();
    }
}
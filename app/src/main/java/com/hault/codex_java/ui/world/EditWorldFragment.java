package com.hault.codex_java.ui.world;

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
import com.hault.codex_java.data.model.World;
import com.hault.codex_java.ui.util.ColorPickerHelper;
import com.hault.codex_java.viewmodel.WorldViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditWorldFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";

    private WorldViewModel worldViewModel;
    private TextInputEditText editTextWorldName;
    private TextInputEditText editTextWorldDescription;
    private TextInputEditText editTextTags;
    private SwitchMaterial switchIsPinned;
    private ImageView imageViewHeader;
    private Button buttonSelectImage;
    private World currentWorld;
    private int worldId;
    private Uri selectedImageUri;
    private ColorPickerHelper colorPickerHelper;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public static EditWorldFragment newInstance(int worldId) {
        EditWorldFragment fragment = new EditWorldFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WORLD_ID, worldId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        worldViewModel = new ViewModelProvider(this).get(WorldViewModel.class);
        if (getArguments() != null) {
            worldId = getArguments().getInt(ARG_WORLD_ID);
        }

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
        return inflater.inflate(R.layout.fragment_edit_world, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextWorldName = view.findViewById(R.id.editTextWorldName);
        editTextWorldDescription = view.findViewById(R.id.editTextWorldDescription);
        editTextTags = view.findViewById(R.id.editTextTags);
        switchIsPinned = view.findViewById(R.id.switchIsPinned);
        Button buttonSaveWorld = view.findViewById(R.id.buttonSaveWorld);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        imageViewHeader = view.findViewById(R.id.imageViewHeader);
        buttonSelectImage = view.findViewById(R.id.buttonSelectImage);

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        worldViewModel.getWorldById(worldId).observe(getViewLifecycleOwner(), world -> {
            if (world != null) {
                currentWorld = world;
                if (selectedImageUri == null && currentWorld.imageUri != null) {
                    selectedImageUri = Uri.parse(currentWorld.imageUri);
                }
                populateUI(view);
            }
        });

        buttonSelectImage.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        buttonSaveWorld.setOnClickListener(v -> saveWorld());
    }

    private void populateUI(View view) {
        editTextWorldName.setText(currentWorld.name);
        editTextWorldDescription.setText(currentWorld.description);
        editTextTags.setText(currentWorld.tags);
        switchIsPinned.setChecked(currentWorld.isPinned);

        if (selectedImageUri != null) {
            Glide.with(this).load(selectedImageUri).centerCrop().into(imageViewHeader);
        }

        LinearLayout colorPickerContainer = view.findViewById(R.id.colorPickerContainer);
        colorPickerHelper = new ColorPickerHelper(colorPickerContainer, currentWorld.colorHex);
    }

    private void saveWorld() {
        String name = editTextWorldName.getText().toString().trim();
        String description = editTextWorldDescription.getText().toString().trim();
        String tags = editTextTags.getText().toString().trim();
        boolean isPinned = switchIsPinned.isChecked();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "World name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        currentWorld.name = name;
        currentWorld.description = description;
        currentWorld.tags = tags;
        currentWorld.isPinned = isPinned;
        if (selectedImageUri != null) {
            currentWorld.imageUri = selectedImageUri.toString();
        } else {
            currentWorld.imageUri = null;
        }
        currentWorld.colorHex = colorPickerHelper.getSelectedColorHex();

        worldViewModel.update(currentWorld);

        getParentFragmentManager().popBackStack();
    }
}
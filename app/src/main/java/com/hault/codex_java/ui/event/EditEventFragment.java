package com.hault.codex_java.ui.event;

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
import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.ui.util.ColorPickerHelper;
import com.hault.codex_java.viewmodel.EventViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditEventFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private static final String ARG_EVENT_ID = "event_id";

    private EventViewModel eventViewModel;
    private TextInputEditText editTextEventName;
    private TextInputEditText editTextEventDate;
    private TextInputEditText editTextEventDescription;
    private TextInputEditText editTextTags;
    private SwitchMaterial switchIsPinned;
    private Event currentEvent;
    private int worldId;
    private int eventId;

    private ImageView imageViewHeader;
    private Button buttonSelectImage;
    private Uri selectedImageUri;
    private ColorPickerHelper colorPickerHelper;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public static EditEventFragment newInstance(int worldId, int eventId) {
        EditEventFragment fragment = new EditEventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WORLD_ID, worldId);
        args.putInt(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            worldId = getArguments().getInt(ARG_WORLD_ID);
            eventId = getArguments().getInt(ARG_EVENT_ID);
        }
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

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
        return inflater.inflate(R.layout.fragment_add_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEventName = view.findViewById(R.id.editTextEventName);
        editTextEventDate = view.findViewById(R.id.editTextEventDate);
        editTextEventDescription = view.findViewById(R.id.editTextEventDescription);
        editTextTags = view.findViewById(R.id.editTextTags);
        switchIsPinned = view.findViewById(R.id.switchIsPinned);
        Button buttonSaveEvent = view.findViewById(R.id.buttonSaveEvent);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        imageViewHeader = view.findViewById(R.id.imageViewHeader);
        buttonSelectImage = view.findViewById(R.id.buttonSelectImage);

        toolbar.setTitle("Edit Event");
        buttonSaveEvent.setText("Save Changes");
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        eventViewModel.getEventById(eventId).observe(getViewLifecycleOwner(), event -> {
            if (event != null) {
                currentEvent = event;
                if (selectedImageUri == null && currentEvent.imageUri != null) {
                    selectedImageUri = Uri.parse(currentEvent.imageUri);
                }
                populateUI(view);
            }
        });

        buttonSelectImage.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        buttonSaveEvent.setOnClickListener(v -> saveEvent());
    }

    private void populateUI(View view) {
        editTextEventName.setText(currentEvent.name);
        editTextEventDate.setText(currentEvent.date);
        editTextEventDescription.setText(currentEvent.description);
        editTextTags.setText(currentEvent.tags);
        switchIsPinned.setChecked(currentEvent.isPinned);

        if (selectedImageUri != null) {
            Glide.with(this).load(selectedImageUri).centerCrop().into(imageViewHeader);
        }

        LinearLayout colorPickerContainer = view.findViewById(R.id.colorPickerContainer);
        colorPickerHelper = new ColorPickerHelper(colorPickerContainer, currentEvent.colorHex);
    }


    private void saveEvent() {
        if (currentEvent == null) return;

        String name = editTextEventName.getText().toString().trim();
        String date = editTextEventDate.getText().toString().trim();
        String description = editTextEventDescription.getText().toString().trim();
        String tags = editTextTags.getText().toString().trim();
        boolean isPinned = switchIsPinned.isChecked();

        if (name.isEmpty() || date.isEmpty()) {
            Toast.makeText(getContext(), "Event name and date are required", Toast.LENGTH_SHORT).show();
            return;
        }

        currentEvent.name = name;
        currentEvent.date = date;
        currentEvent.description = description;
        currentEvent.tags = tags;
        currentEvent.isPinned = isPinned;
        if (selectedImageUri != null) {
            currentEvent.imageUri = selectedImageUri.toString();
        } else {
            currentEvent.imageUri = null;
        }
        currentEvent.colorHex = colorPickerHelper.getSelectedColorHex();

        eventViewModel.update(currentEvent);
        getParentFragmentManager().popBackStack();
    }
}
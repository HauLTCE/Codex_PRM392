package com.hault.codex_java.ui.arc;

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
import com.hault.codex_java.data.model.Arc;
import com.hault.codex_java.ui.util.ColorPickerHelper;
import com.hault.codex_java.viewmodel.ArcViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditArcFragment extends Fragment {

    private static final String ARG_ARC_ID = "arc_id";
    private ArcViewModel arcViewModel;
    private TextInputEditText editTextArcTitle;
    private TextInputEditText editTextArcSynopsis;
    private TextInputEditText editTextTags;
    private SwitchMaterial switchIsPinned;
    private Arc currentArc;
    private int arcId;

    private ImageView imageViewHeader;
    private Button buttonSelectImage;
    private Uri selectedImageUri;
    private ColorPickerHelper colorPickerHelper;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public static EditArcFragment newInstance(int arcId) {
        EditArcFragment fragment = new EditArcFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ARC_ID, arcId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            arcId = getArguments().getInt(ARG_ARC_ID);
        }
        arcViewModel = new ViewModelProvider(this).get(ArcViewModel.class);

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
        return inflater.inflate(R.layout.fragment_add_edit_arc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextArcTitle = view.findViewById(R.id.editTextArcTitle);
        editTextArcSynopsis = view.findViewById(R.id.editTextArcSynopsis);
        editTextTags = view.findViewById(R.id.editTextTags);
        switchIsPinned = view.findViewById(R.id.switchIsPinned);
        Button buttonSaveArc = view.findViewById(R.id.buttonSaveArc);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        imageViewHeader = view.findViewById(R.id.imageViewHeader);
        buttonSelectImage = view.findViewById(R.id.buttonSelectImage);

        toolbar.setTitle("Edit Story Arc");
        buttonSaveArc.setText("Save Changes");

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        arcViewModel.getArcById(arcId).observe(getViewLifecycleOwner(), arc -> {
            if (arc != null) {
                currentArc = arc;
                if (selectedImageUri == null && currentArc.imageUri != null) {
                    selectedImageUri = Uri.parse(currentArc.imageUri);
                }
                populateUI(view);
            }
        });

        buttonSelectImage.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        buttonSaveArc.setOnClickListener(v -> saveArc());
    }

    private void populateUI(View view) {
        editTextArcTitle.setText(currentArc.title);
        editTextArcSynopsis.setText(currentArc.synopsis);
        editTextTags.setText(currentArc.tags);
        switchIsPinned.setChecked(currentArc.isPinned);

        if (selectedImageUri != null) {
            Glide.with(this).load(selectedImageUri).centerCrop().into(imageViewHeader);
        }

        LinearLayout colorPickerContainer = view.findViewById(R.id.colorPickerContainer);
        colorPickerHelper = new ColorPickerHelper(colorPickerContainer, currentArc.colorHex);
    }

    private void saveArc() {
        if (currentArc == null) return;

        String title = editTextArcTitle.getText().toString().trim();
        String synopsis = editTextArcSynopsis.getText().toString().trim();
        String tags = editTextTags.getText().toString().trim();
        boolean isPinned = switchIsPinned.isChecked();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Arc title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        currentArc.title = title;
        currentArc.synopsis = synopsis;
        currentArc.tags = tags;
        currentArc.isPinned = isPinned;
        if (selectedImageUri != null) {
            currentArc.imageUri = selectedImageUri.toString();
        } else {
            currentArc.imageUri = null;
        }
        currentArc.colorHex = colorPickerHelper.getSelectedColorHex();

        arcViewModel.update(currentArc);
        getParentFragmentManager().popBackStack();
    }
}
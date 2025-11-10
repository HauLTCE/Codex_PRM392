package com.hault.codex_java.ui.chapter;

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
import com.hault.codex_java.data.model.Chapter;
import com.hault.codex_java.ui.util.ColorPickerHelper;
import com.hault.codex_java.viewmodel.ChapterViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditChapterFragment extends Fragment {

    private static final String ARG_CHAPTER_ID = "chapter_id";

    private ChapterViewModel chapterViewModel;
    private TextInputEditText editTextChapterTitle;
    private TextInputEditText editTextChapterNumber;
    private TextInputEditText editTextChapterProse;
    private TextInputEditText editTextTags;
    private SwitchMaterial switchIsPinned;
    private Chapter currentChapter;
    private int chapterId;

    private ImageView imageViewHeader;
    private Button buttonSelectImage;
    private Uri selectedImageUri;
    private ColorPickerHelper colorPickerHelper;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public static EditChapterFragment newInstance(int chapterId) {
        EditChapterFragment fragment = new EditChapterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHAPTER_ID, chapterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chapterId = getArguments().getInt(ARG_CHAPTER_ID);
        }
        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);

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
        return inflater.inflate(R.layout.fragment_add_edit_chapter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextChapterTitle = view.findViewById(R.id.editTextChapterTitle);
        editTextChapterNumber = view.findViewById(R.id.editTextChapterNumber);
        editTextChapterProse = view.findViewById(R.id.editTextChapterProse);
        editTextTags = view.findViewById(R.id.editTextTags);
        switchIsPinned = view.findViewById(R.id.switchIsPinned);
        Button buttonSaveChapter = view.findViewById(R.id.buttonSaveChapter);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        imageViewHeader = view.findViewById(R.id.imageViewHeader);
        buttonSelectImage = view.findViewById(R.id.buttonSelectImage);

        toolbar.setTitle("Edit Chapter");
        buttonSaveChapter.setText("Save Changes");
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        chapterViewModel.getChapterById(chapterId).observe(getViewLifecycleOwner(), chapter -> {
            if (chapter != null) {
                currentChapter = chapter;
                if (selectedImageUri == null && currentChapter.imageUri != null) {
                    selectedImageUri = Uri.parse(currentChapter.imageUri);
                }
                populateUI(view);
            }
        });

        buttonSelectImage.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        buttonSaveChapter.setOnClickListener(v -> saveChapter());
    }

    private void populateUI(View view) {
        editTextChapterTitle.setText(currentChapter.title);
        editTextChapterNumber.setText(String.valueOf(currentChapter.chapterNumber));
        editTextChapterProse.setText(currentChapter.prose);
        editTextTags.setText(currentChapter.tags);
        switchIsPinned.setChecked(currentChapter.isPinned);

        if (selectedImageUri != null) {
            Glide.with(this).load(selectedImageUri).centerCrop().into(imageViewHeader);
        }

        LinearLayout colorPickerContainer = view.findViewById(R.id.colorPickerContainer);
        colorPickerHelper = new ColorPickerHelper(colorPickerContainer, currentChapter.colorHex);
    }

    private void saveChapter() {
        if (currentChapter == null) return;

        String title = editTextChapterTitle.getText().toString().trim();
        String numberStr = editTextChapterNumber.getText().toString().trim();
        String prose = editTextChapterProse.getText().toString().trim();
        String tags = editTextTags.getText().toString().trim();
        boolean isPinned = switchIsPinned.isChecked();


        if (title.isEmpty() || numberStr.isEmpty()) {
            Toast.makeText(getContext(), "Chapter title and number are required", Toast.LENGTH_SHORT).show();
            return;
        }

        currentChapter.title = title;
        currentChapter.chapterNumber = Integer.parseInt(numberStr);
        currentChapter.prose = prose;
        currentChapter.tags = tags;
        currentChapter.isPinned = isPinned;
        if (selectedImageUri != null) {
            currentChapter.imageUri = selectedImageUri.toString();
        } else {
            currentChapter.imageUri = null;
        }
        currentChapter.colorHex = colorPickerHelper.getSelectedColorHex();

        chapterViewModel.update(currentChapter);
        getParentFragmentManager().popBackStack();
    }
}
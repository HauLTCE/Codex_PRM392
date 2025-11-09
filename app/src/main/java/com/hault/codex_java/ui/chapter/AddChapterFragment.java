package com.hault.codex_java.ui.chapter;

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
import com.hault.codex_java.data.model.Chapter;
import com.hault.codex_java.viewmodel.ChapterViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddChapterFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private static final String ARG_ARC_ID = "arc_id";

    private ChapterViewModel chapterViewModel;
    private TextInputEditText editTextChapterTitle;
    private TextInputEditText editTextChapterNumber;
    private TextInputEditText editTextChapterProse;
    private TextInputEditText editTextTags;
    private SwitchMaterial switchIsPinned;
    private int worldId;
    private int arcId;

    public static AddChapterFragment newInstance(int worldId, int arcId) {
        AddChapterFragment fragment = new AddChapterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WORLD_ID, worldId);
        args.putInt(ARG_ARC_ID, arcId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            worldId = getArguments().getInt(ARG_WORLD_ID);
            arcId = getArguments().getInt(ARG_ARC_ID);
        }
        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);
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
        toolbar.setTitle("Add Chapter");

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        buttonSaveChapter.setOnClickListener(v -> saveChapter());
    }

    private void saveChapter() {
        String title = editTextChapterTitle.getText().toString().trim();
        String numberStr = editTextChapterNumber.getText().toString().trim();
        String prose = editTextChapterProse.getText().toString().trim();
        String tags = editTextTags.getText().toString().trim();
        boolean isPinned = switchIsPinned.isChecked();


        if (title.isEmpty() || numberStr.isEmpty()) {
            Toast.makeText(getContext(), "Chapter title and number are required", Toast.LENGTH_SHORT).show();
            return;
        }

        int chapterNumber = Integer.parseInt(numberStr);
        Chapter newChapter = new Chapter(arcId, worldId, title, chapterNumber, prose);
        newChapter.tags = tags;
        newChapter.isPinned = isPinned;

        chapterViewModel.insert(newChapter);
        getParentFragmentManager().popBackStack();
    }
}
package com.hault.codex_java.ui.arc;

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
import com.hault.codex_java.data.model.Arc;
import com.hault.codex_java.viewmodel.ArcViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddArcFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private ArcViewModel arcViewModel;
    private TextInputEditText editTextArcTitle;
    private TextInputEditText editTextArcSynopsis;
    private TextInputEditText editTextTags;
    private SwitchMaterial switchIsPinned;
    private int worldId;

    public static AddArcFragment newInstance(int worldId) {
        AddArcFragment fragment = new AddArcFragment();
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
        arcViewModel = new ViewModelProvider(this).get(ArcViewModel.class);
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
        toolbar.setTitle("Add Story Arc");

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        buttonSaveArc.setOnClickListener(v -> saveArc());
    }

    private void saveArc() {
        String title = editTextArcTitle.getText().toString().trim();
        String synopsis = editTextArcSynopsis.getText().toString().trim();
        String tags = editTextTags.getText().toString().trim();
        boolean isPinned = switchIsPinned.isChecked();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Arc title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Arc newArc = new Arc(worldId, title, synopsis, 0); // Order is not implemented yet
        newArc.tags = tags;
        newArc.isPinned = isPinned;

        arcViewModel.insert(newArc);
        getParentFragmentManager().popBackStack();
    }
}
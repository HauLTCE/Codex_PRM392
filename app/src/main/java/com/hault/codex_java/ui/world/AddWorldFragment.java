package com.hault.codex_java.ui.world;

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
import com.hault.codex_java.data.model.World;
import com.hault.codex_java.viewmodel.WorldViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddWorldFragment extends Fragment {

    private WorldViewModel worldViewModel;
    private TextInputEditText editTextWorldName;
    private TextInputEditText editTextWorldDescription;
    private TextInputEditText editTextTags;
    private SwitchMaterial switchIsPinned;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        worldViewModel = new ViewModelProvider(this).get(WorldViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_world, container, false);
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

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        buttonSaveWorld.setOnClickListener(v -> saveWorld());
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

        World newWorld = new World(name, description);
        newWorld.tags = tags;
        newWorld.isPinned = isPinned;

        worldViewModel.insert(newWorld);

        getParentFragmentManager().popBackStack();
    }
}
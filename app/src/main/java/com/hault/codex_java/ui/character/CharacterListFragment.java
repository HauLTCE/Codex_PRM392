package com.hault.codex_java.ui.character;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.viewmodel.CharacterViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CharacterListFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private int worldId;
    private CharacterViewModel characterViewModel;
    private CharacterListAdapter adapter;

    public static CharacterListFragment newInstance(int worldId) {
        CharacterListFragment fragment = new CharacterListFragment();
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        characterViewModel = new ViewModelProvider(requireActivity()).get(CharacterViewModel.class);
        characterViewModel.setWorldId(worldId);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCharacters);
        adapter = new CharacterListAdapter();
        recyclerView.setAdapter(adapter);

        characterViewModel.getCharacters().observe(getViewLifecycleOwner(), characters -> {
            adapter.submitList(characters);
        });

        FloatingActionButton fab = view.findViewById(R.id.fabAddCharacter);
        fab.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, AddCharacterFragment.newInstance(worldId))
                    .addToBackStack(null)
                    .commit();
        });

        adapter.setOnItemClickListener(character -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, EditCharacterFragment.newInstance(worldId, character.id))
                    .addToBackStack(null)
                    .commit();
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Character characterToDelete = adapter.getCharacterAt(position);
                characterViewModel.delete(characterToDelete);
                Snackbar.make(view, "Character deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> characterViewModel.insert(characterToDelete))
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}
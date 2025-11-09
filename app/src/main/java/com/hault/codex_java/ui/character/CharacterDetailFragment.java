package com.hault.codex_java.ui.character;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.relations.CharacterWithDetails;
import com.hault.codex_java.ui.event.EventListAdapter;
import com.hault.codex_java.viewmodel.CharacterViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CharacterDetailFragment extends Fragment {

    private static final String ARG_CHARACTER_ID = "character_id";
    private int characterId;

    private CharacterViewModel characterViewModel;
    private CharacterWithDetails currentCharacterDetails;

    private MaterialToolbar toolbar;
    private TextView tvHomeLocation, tvBackstory;
    private CardView cardHomeLocation;
    private RecyclerView rvRelationships, rvEvents;
    private RelationshipListAdapter relationshipAdapter;
    private EventListAdapter eventAdapter;


    public static CharacterDetailFragment newInstance(int characterId) {
        CharacterDetailFragment fragment = new CharacterDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHARACTER_ID, characterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            characterId = getArguments().getInt(ARG_CHARACTER_ID);
        }
        characterViewModel = new ViewModelProvider(this).get(CharacterViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupToolbar();
        setupRecyclerViews();

        characterViewModel.getCharacterWithDetails(characterId).observe(getViewLifecycleOwner(), characterWithDetails -> {
            if (characterWithDetails != null) {
                this.currentCharacterDetails = characterWithDetails;
                updateUI();
            }
        });
    }

    private void bindViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tvHomeLocation = view.findViewById(R.id.textViewHomeLocation);
        tvBackstory = view.findViewById(R.id.textViewBackstory);
        cardHomeLocation = view.findViewById(R.id.cardHomeLocation);
        rvRelationships = view.findViewById(R.id.recyclerViewRelationships);
        rvEvents = view.findViewById(R.id.recyclerViewEvents);
    }

    private void setupRecyclerViews() {
        relationshipAdapter = new RelationshipListAdapter();
        rvRelationships.setAdapter(relationshipAdapter);

        eventAdapter = new EventListAdapter();
        rvEvents.setAdapter(eventAdapter);
    }

    private void updateUI() {
        toolbar.setTitle(currentCharacterDetails.character.name);
        tvBackstory.setText(currentCharacterDetails.character.backstory);

        if (currentCharacterDetails.homeLocation != null) {
            cardHomeLocation.setVisibility(View.VISIBLE);
            tvHomeLocation.setText(currentCharacterDetails.homeLocation.name);
        } else {
            cardHomeLocation.setVisibility(View.GONE);
        }

        // We need to fetch the relationships from the CrossRef table, not the direct relation
        // This is a limitation in Room. We will query the cross-refs separately.
        // For now, this part will be placeholder. We will implement the query in the next step.
        // relationshipAdapter.setRelationships(currentCharacterDetails.relationships, currentCharacterDetails.relatedCharacters);

        eventAdapter.submitList(currentCharacterDetails.events);
    }


    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_edit) {
                navigateToEdit();
                return true;
            } else if (itemId == R.id.action_delete) {
                confirmDelete();
                return true;
            }
            return false;
        });
    }

    private void navigateToEdit() {
        if (currentCharacterDetails != null) {
            // We need the worldId for the edit fragment
            int worldId = currentCharacterDetails.character.worldId;
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, EditCharacterFragment.newInstance(worldId, characterId))
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void confirmDelete() {
        if (currentCharacterDetails == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Character")
                .setMessage("Are you sure you want to delete '" + currentCharacterDetails.character.name + "'? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    characterViewModel.delete(currentCharacterDetails.character);
                    getParentFragmentManager().popBackStack();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
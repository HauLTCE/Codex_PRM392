package com.hault.codex_java.ui.character;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.data.model.crossref.CharacterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventCharacterCrossRef;
import com.hault.codex_java.data.model.relations.CharacterWithDetails;
import com.hault.codex_java.ui.dialog.ItemRoleSelectorDialogFragment;
import com.hault.codex_java.ui.dialog.RelationshipBuilderDialogFragment;
import com.hault.codex_java.ui.event.EventListAdapter;
import com.hault.codex_java.viewmodel.CharacterViewModel;
import com.hault.codex_java.viewmodel.CrossRefViewModel;
import com.hault.codex_java.viewmodel.EventViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CharacterDetailFragment extends Fragment {

    private static final String ARG_CHARACTER_ID = "character_id";
    private static final String REQUEST_KEY_SELECT_EVENTS = "requestKey_character_selectEvents";
    private static final String REQUEST_KEY_RELATIONSHIP = "requestKey_addRelationship";
    private int characterId;

    private CharacterViewModel characterViewModel;
    private EventViewModel eventViewModel;
    private CrossRefViewModel crossRefViewModel;

    private CharacterWithDetails currentCharacterDetails;

    private MaterialToolbar toolbar;
    private ImageView imageViewHeader;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView tvHomeLocation, tvBackstory;
    private CardView cardHomeLocation;
    private RecyclerView rvRelationships, rvEvents;
    private RelationshipListAdapter relationshipAdapter;
    private EventListAdapter eventAdapter;
    private ImageButton btnAddRelationship, btnAddEvent;


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
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        crossRefViewModel = new ViewModelProvider(this).get(CrossRefViewModel.class);

        setupFragmentResultListeners();
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
        setupClickListeners();

        characterViewModel.getCharacterWithDetails(characterId).observe(getViewLifecycleOwner(), characterWithDetails -> {
            if (characterWithDetails != null) {
                this.currentCharacterDetails = characterWithDetails;
                updateUI();
            }
        });

        crossRefViewModel.getRelationshipsForCharacter(characterId).observe(getViewLifecycleOwner(), crossRefs -> {
            if (crossRefs != null && currentCharacterDetails != null) {
                updateRelationshipAdapter(crossRefs, currentCharacterDetails.relatedCharacters);
            }
        });
    }

    private void bindViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        imageViewHeader = view.findViewById(R.id.imageViewHeader);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) appBarLayout.getChildAt(0);
        tvHomeLocation = view.findViewById(R.id.textViewHomeLocation);
        tvBackstory = view.findViewById(R.id.textViewBackstory);
        cardHomeLocation = view.findViewById(R.id.cardHomeLocation);
        rvRelationships = view.findViewById(R.id.recyclerViewRelationships);
        rvEvents = view.findViewById(R.id.recyclerViewEvents);
        btnAddRelationship = view.findViewById(R.id.btnAddRelationship);
        btnAddEvent = view.findViewById(R.id.btnAddEvent);
    }

    private void setupRecyclerViews() {
        relationshipAdapter = new RelationshipListAdapter();
        rvRelationships.setAdapter(relationshipAdapter);

        eventAdapter = new EventListAdapter();
        rvEvents.setAdapter(eventAdapter);
    }

    private void updateUI() {
        if (currentCharacterDetails == null) return;
        toolbar.setTitle(currentCharacterDetails.character.name);
        tvBackstory.setText(currentCharacterDetails.character.backstory);

        if (currentCharacterDetails.homeLocation != null) {
            cardHomeLocation.setVisibility(View.VISIBLE);
            tvHomeLocation.setText(currentCharacterDetails.homeLocation.name);
        } else {
            cardHomeLocation.setVisibility(View.GONE);
        }

        if (currentCharacterDetails.character.imageUri != null && !currentCharacterDetails.character.imageUri.isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(currentCharacterDetails.character.imageUri))
                    .centerCrop()
                    .into(imageViewHeader);
        }

        if (currentCharacterDetails.character.colorHex != null && !currentCharacterDetails.character.colorHex.isEmpty()) {
            int color = Color.parseColor(currentCharacterDetails.character.colorHex);
            collapsingToolbarLayout.setContentScrimColor(color);
            appBarLayout.setBackgroundColor(color);
        }


        eventAdapter.submitList(currentCharacterDetails.events);
    }

    private void updateRelationshipAdapter(List<CharacterCharacterCrossRef> crossRefs, List<Character> relatedCharacters) {
        List<RelationshipListAdapter.RelationshipDisplayItem> displayItems = new ArrayList<>();
        for (CharacterCharacterCrossRef ref : crossRefs) {
            Character target = relatedCharacters.stream()
                    .filter(c -> c.id == ref.characterTwoId)
                    .findFirst()
                    .orElse(null);
            if (target != null) {
                displayItems.add(new RelationshipListAdapter.RelationshipDisplayItem(ref, target));
            }
        }
        relationshipAdapter.submitList(displayItems);
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

    private void setupClickListeners() {
        btnAddEvent.setOnClickListener(v -> openEventSelector());
        btnAddRelationship.setOnClickListener(v -> openRelationshipBuilderForAdd());

        relationshipAdapter.setOnItemClickListener(item -> {
            RelationshipBuilderDialogFragment.newInstanceForEdit(
                    item.crossRef.characterOneId,
                    currentCharacterDetails.character.name,
                    item.targetCharacter.name,
                    item.targetCharacter.id,
                    item.crossRef.relationshipDescription,
                    REQUEST_KEY_RELATIONSHIP
            ).show(getParentFragmentManager(), RelationshipBuilderDialogFragment.TAG);
        });
    }

    private void openRelationshipBuilderForAdd() {
        if (currentCharacterDetails == null) return;
        int worldId = currentCharacterDetails.character.worldId;

        characterViewModel.getCharactersForWorld(worldId).observe(getViewLifecycleOwner(), new Observer<List<Character>>() {
            @Override
            public void onChanged(List<Character> allCharacters) {
                characterViewModel.getCharactersForWorld(worldId).removeObserver(this);

                List<Integer> existingTargetIds = currentCharacterDetails.relatedCharacters.stream()
                        .map(c -> c.id)
                        .collect(Collectors.toList());

                List<Character> potentialTargets = allCharacters.stream()
                        .filter(c -> c.id != characterId && !existingTargetIds.contains(c.id))
                        .collect(Collectors.toList());

                if (potentialTargets.isEmpty()) {
                    Toast.makeText(getContext(), "No new characters available to form a relationship.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<String> names = new ArrayList<>();
                ArrayList<Integer> ids = new ArrayList<>();
                for(Character character : potentialTargets) {
                    names.add(character.name);
                    ids.add(character.id);
                }

                RelationshipBuilderDialogFragment.newInstanceForAdd(
                        characterId,
                        currentCharacterDetails.character.name,
                        names,
                        ids,
                        REQUEST_KEY_RELATIONSHIP
                ).show(getParentFragmentManager(), RelationshipBuilderDialogFragment.TAG);
            }
        });
    }

    private void openEventSelector() {
        if (currentCharacterDetails == null) return;
        int worldId = currentCharacterDetails.character.worldId;

        eventViewModel.getEventsForWorld(worldId).observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventViewModel.getEventsForWorld(worldId).removeObserver(this);
                ArrayList<String> names = new ArrayList<>();
                ArrayList<Integer> ids = new ArrayList<>();
                for(Event event : events) {
                    names.add(event.name);
                    ids.add(event.id);
                }
                ItemRoleSelectorDialogFragment.newInstance("Link Events", names, ids, REQUEST_KEY_SELECT_EVENTS, "Role in event")
                        .show(getParentFragmentManager(), ItemRoleSelectorDialogFragment.TAG);
            }
        });
    }

    private void setupFragmentResultListeners() {
        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_SELECT_EVENTS, this, (requestKey, bundle) -> {
            ArrayList<Integer> selectedIds = bundle.getIntegerArrayList(ItemRoleSelectorDialogFragment.RESULT_KEY_SELECTED_IDS);
            String role = bundle.getString(ItemRoleSelectorDialogFragment.RESULT_KEY_ROLE);
            if (selectedIds != null && role != null) {
                for (Integer eventId : selectedIds) {
                    EventCharacterCrossRef crossRef = new EventCharacterCrossRef(eventId, characterId, role);
                    crossRefViewModel.insert(crossRef);
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_RELATIONSHIP, this, (requestKey, bundle) -> {
            String action = bundle.getString(RelationshipBuilderDialogFragment.BUNDLE_KEY_ACTION);
            int sourceId = bundle.getInt(RelationshipBuilderDialogFragment.BUNDLE_KEY_SOURCE_ID);
            int targetId = bundle.getInt(RelationshipBuilderDialogFragment.BUNDLE_KEY_TARGET_ID);
            String description = bundle.getString(RelationshipBuilderDialogFragment.BUNDLE_KEY_DESCRIPTION);

            CharacterCharacterCrossRef crossRef = new CharacterCharacterCrossRef(sourceId, targetId, description);

            switch (Objects.requireNonNull(action)) {
                case RelationshipBuilderDialogFragment.ACTION_SAVE:
                    crossRefViewModel.insert(crossRef);
                    break;
                case RelationshipBuilderDialogFragment.ACTION_UPDATE:
                    crossRefViewModel.update(crossRef);
                    break;
                case RelationshipBuilderDialogFragment.ACTION_DELETE:
                    crossRefViewModel.delete(crossRef);
                    break;
            }
        });
    }

    private void navigateToEdit() {
        if (currentCharacterDetails != null) {
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
package com.hault.codex_java.ui.chapter;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.data.model.crossref.ChapterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterLocationCrossRef;
import com.hault.codex_java.data.model.relations.ChapterWithDetails;
import com.hault.codex_java.ui.character.CharacterListAdapter;
import com.hault.codex_java.ui.dialog.ItemRoleSelectorDialogFragment;
import com.hault.codex_java.ui.location.LocationListAdapter;
import com.hault.codex_java.viewmodel.CharacterViewModel;
import com.hault.codex_java.viewmodel.ChapterViewModel;
import com.hault.codex_java.viewmodel.CrossRefViewModel;
import com.hault.codex_java.viewmodel.LocationViewModel;
import java.util.ArrayList;
import java.util.List;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChapterDetailFragment extends Fragment {

    private static final String ARG_CHAPTER_ID = "chapter_id";
    private static final String REQUEST_KEY_SELECT_CHARS = "requestKey_chapter_selectChars";
    private static final String REQUEST_KEY_SELECT_LOCATIONS = "requestKey_chapter_selectLocations";
    private int chapterId;

    private ChapterViewModel chapterViewModel;
    private CharacterViewModel characterViewModel;
    private LocationViewModel locationViewModel;
    private CrossRefViewModel crossRefViewModel;
    private ChapterWithDetails currentChapterDetails;

    private MaterialToolbar toolbar;
    private ImageView imageViewHeader;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView tvProse;
    private RecyclerView rvCharacters, rvLocations;
    private CharacterListAdapter characterAdapter;
    private LocationListAdapter locationAdapter;
    private ImageButton btnAddCharacter, btnAddLocation;

    public static ChapterDetailFragment newInstance(int chapterId) {
        ChapterDetailFragment fragment = new ChapterDetailFragment();
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
        characterViewModel = new ViewModelProvider(this).get(CharacterViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        crossRefViewModel = new ViewModelProvider(this).get(CrossRefViewModel.class);
        setupFragmentResultListeners();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chapter_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupToolbar();
        setupRecyclerViews();
        setupClickListeners();

        chapterViewModel.getChapterWithDetails(chapterId).observe(getViewLifecycleOwner(), chapterWithDetails -> {
            if (chapterWithDetails != null) {
                this.currentChapterDetails = chapterWithDetails;
                updateUI();
            }
        });
    }

    private void bindViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        imageViewHeader = view.findViewById(R.id.imageViewHeader);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) appBarLayout.getChildAt(0);
        tvProse = view.findViewById(R.id.textViewProse);
        rvCharacters = view.findViewById(R.id.recyclerViewCharacters);
        rvLocations = view.findViewById(R.id.recyclerViewLocations);
        btnAddCharacter = view.findViewById(R.id.btnAddCharacter);
        btnAddLocation = view.findViewById(R.id.btnAddLocation);
    }

    private void setupRecyclerViews() {
        characterAdapter = new CharacterListAdapter();
        rvCharacters.setAdapter(characterAdapter);

        locationAdapter = new LocationListAdapter();
        rvLocations.setAdapter(locationAdapter);
    }

    private void updateUI() {
        toolbar.setTitle(currentChapterDetails.chapter.title);
        toolbar.setSubtitle("Chapter " + currentChapterDetails.chapter.chapterNumber);
        tvProse.setText(currentChapterDetails.chapter.prose);
        characterAdapter.submitList(currentChapterDetails.characters);
        locationAdapter.submitList(currentChapterDetails.locations);

        if (currentChapterDetails.chapter.imageUri != null && !currentChapterDetails.chapter.imageUri.isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(currentChapterDetails.chapter.imageUri))
                    .centerCrop()
                    .into(imageViewHeader);
        }

        if (currentChapterDetails.chapter.colorHex != null && !currentChapterDetails.chapter.colorHex.isEmpty()) {
            int color = Color.parseColor(currentChapterDetails.chapter.colorHex);
            collapsingToolbarLayout.setContentScrimColor(color);
            appBarLayout.setBackgroundColor(color);
        }
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
        btnAddCharacter.setOnClickListener(v -> openCharacterSelector());
        btnAddLocation.setOnClickListener(v -> openLocationSelector());
    }

    private void openCharacterSelector() {
        if (currentChapterDetails == null) return;
        int worldId = currentChapterDetails.chapter.worldId;

        characterViewModel.getCharactersForWorld(worldId).observe(getViewLifecycleOwner(), new Observer<List<Character>>() {
            @Override
            public void onChanged(List<Character> characters) {
                characterViewModel.getCharactersForWorld(worldId).removeObserver(this);
                ArrayList<String> names = new ArrayList<>();
                ArrayList<Integer> ids = new ArrayList<>();
                for(Character character : characters) {
                    names.add(character.name);
                    ids.add(character.id);
                }
                ItemRoleSelectorDialogFragment.newInstance("Feature Characters", names, ids, REQUEST_KEY_SELECT_CHARS, "Role in chapter")
                        .show(getParentFragmentManager(), ItemRoleSelectorDialogFragment.TAG);
            }
        });
    }

    private void openLocationSelector() {
        if (currentChapterDetails == null) return;
        int worldId = currentChapterDetails.chapter.worldId;

        locationViewModel.getLocationsForWorld(worldId).observe(getViewLifecycleOwner(), new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                locationViewModel.getLocationsForWorld(worldId).removeObserver(this);
                ArrayList<String> names = new ArrayList<>();
                ArrayList<Integer> ids = new ArrayList<>();
                for(Location location : locations) {
                    names.add(location.name);
                    ids.add(location.id);
                }
                ItemRoleSelectorDialogFragment.newInstance("Feature Locations", names, ids, REQUEST_KEY_SELECT_LOCATIONS, "Role of location")
                        .show(getParentFragmentManager(), ItemRoleSelectorDialogFragment.TAG);
            }
        });
    }

    private void setupFragmentResultListeners() {
        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_SELECT_CHARS, this, (requestKey, bundle) -> {
            ArrayList<Integer> selectedIds = bundle.getIntegerArrayList(ItemRoleSelectorDialogFragment.RESULT_KEY_SELECTED_IDS);
            String role = bundle.getString(ItemRoleSelectorDialogFragment.RESULT_KEY_ROLE);
            if (selectedIds != null && role != null) {
                for (Integer charId : selectedIds) {
                    ChapterCharacterCrossRef crossRef = new ChapterCharacterCrossRef(chapterId, charId, role);
                    crossRefViewModel.insert(crossRef);
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_SELECT_LOCATIONS, this, (requestKey, bundle) -> {
            ArrayList<Integer> selectedIds = bundle.getIntegerArrayList(ItemRoleSelectorDialogFragment.RESULT_KEY_SELECTED_IDS);
            String role = bundle.getString(ItemRoleSelectorDialogFragment.RESULT_KEY_ROLE);
            if (selectedIds != null && role != null) {
                for (Integer locId : selectedIds) {
                    ChapterLocationCrossRef crossRef = new ChapterLocationCrossRef(chapterId, locId, role);
                    crossRefViewModel.insert(crossRef);
                }
            }
        });
    }

    private void navigateToEdit() {
        if (currentChapterDetails != null) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, EditChapterFragment.newInstance(chapterId))
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void confirmDelete() {
        if (currentChapterDetails == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Chapter")
                .setMessage("Are you sure you want to delete '" + currentChapterDetails.chapter.title + "'? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    chapterViewModel.delete(currentChapterDetails.chapter);
                    getParentFragmentManager().popBackStack();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
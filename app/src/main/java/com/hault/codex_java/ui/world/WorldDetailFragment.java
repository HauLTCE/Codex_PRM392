package com.hault.codex_java.ui.world;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hault.codex_java.R;
import com.hault.codex_java.ui.arc.AddArcFragment;
import com.hault.codex_java.ui.character.AddCharacterFragment;
import com.hault.codex_java.ui.event.AddEventFragment;
import com.hault.codex_java.ui.location.AddLocationFragment;
import com.hault.codex_java.viewmodel.ArcViewModel;
import com.hault.codex_java.viewmodel.CharacterViewModel;
import com.hault.codex_java.viewmodel.EventViewModel;
import com.hault.codex_java.viewmodel.LocationViewModel;
import com.hault.codex_java.viewmodel.WorldViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorldDetailFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private static final String ARG_START_TAB_INDEX = "start_tab_index";
    private int worldId;
    private int startTabIndex;
    private WorldViewModel worldViewModel;
    private CharacterViewModel characterViewModel;
    private LocationViewModel locationViewModel;
    private EventViewModel eventViewModel;
    private ArcViewModel arcViewModel;
    private TextInputEditText searchEditText;
    private TextInputLayout searchInputLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton fab;
    private TextWatcher currentTextWatcher;

    public static WorldDetailFragment newInstance(int worldId) {
        return newInstance(worldId, 0);
    }

    public static WorldDetailFragment newInstance(int worldId, int startTabIndex) {
        WorldDetailFragment fragment = new WorldDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WORLD_ID, worldId);
        args.putInt(ARG_START_TAB_INDEX, startTabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            worldId = getArguments().getInt(ARG_WORLD_ID);
            startTabIndex = getArguments().getInt(ARG_START_TAB_INDEX);
        }
        worldViewModel = new ViewModelProvider(requireActivity()).get(WorldViewModel.class);
        characterViewModel = new ViewModelProvider(requireActivity()).get(CharacterViewModel.class);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        arcViewModel = new ViewModelProvider(requireActivity()).get(ArcViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_world_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        searchEditText = view.findViewById(R.id.search_edit_text);
        searchInputLayout = view.findViewById(R.id.search_input_layout);
        fab = view.findViewById(R.id.fabAddItem);


        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        worldViewModel.getWorldById(worldId).observe(getViewLifecycleOwner(), world -> {
            if (world != null) {
                toolbar.setTitle(world.name);
            }
        });

        setupToolbarMenuActions(toolbar);

        WorldDetailViewPagerAdapter adapter = new WorldDetailViewPagerAdapter(requireActivity(), worldId);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Characters"); break;
                case 1: tab.setText("Locations"); break;
                case 2: tab.setText("Events"); break;
                case 3: tab.setText("Arcs"); break;
            }
        }).attach();

        viewPager.setCurrentItem(startTabIndex, false);

        setupDynamicSearchAndFab();
    }

    private void setupDynamicSearchAndFab() {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateSearchAndFab(position);
            }
        });
        // Initial setup for the first tab
        updateSearchAndFab(0);
    }

    private void updateSearchAndFab(int position) {
        if (currentTextWatcher != null) {
            searchEditText.removeTextChangedListener(currentTextWatcher);
        }
        searchEditText.setText("");

        switch (position) {
            case 0: // Characters
                searchInputLayout.setHint("Search Characters...");
                currentTextWatcher = createTextWatcher(s -> characterViewModel.setSearchQuery(s));
                fab.setOnClickListener(v -> navigateToAddFragment(AddCharacterFragment.newInstance(worldId)));
                break;
            case 1: // Locations
                searchInputLayout.setHint("Search Locations...");
                currentTextWatcher = createTextWatcher(s -> locationViewModel.setSearchQuery(s));
                fab.setOnClickListener(v -> navigateToAddFragment(AddLocationFragment.newInstance(worldId)));
                break;
            case 2: // Events
                searchInputLayout.setHint("Search Events...");
                currentTextWatcher = createTextWatcher(s -> eventViewModel.setSearchQuery(s));
                fab.setOnClickListener(v -> navigateToAddFragment(AddEventFragment.newInstance(worldId)));
                break;
            case 3: // Arcs
                searchInputLayout.setHint("Search Arcs...");
                currentTextWatcher = createTextWatcher(s -> arcViewModel.setSearchQuery(s));
                fab.setOnClickListener(v -> navigateToAddFragment(AddArcFragment.newInstance(worldId)));
                break;
        }
        searchEditText.addTextChangedListener(currentTextWatcher);
    }

    private TextWatcher createTextWatcher(SearchQueryUpdater updater) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updater.update(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    private void navigateToAddFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void setupToolbarMenuActions(MaterialToolbar toolbar) {
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit_world) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, EditWorldFragment.newInstance(worldId))
                        .addToBackStack(null)
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.action_delete_world) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete World")
                        .setMessage("Are you sure you want to delete this world and all its contents? This action cannot be undone.")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            worldViewModel.getWorldById(worldId).observe(getViewLifecycleOwner(), worldToDelete -> {
                                if (worldToDelete != null) {
                                    worldViewModel.delete(worldToDelete);
                                    getParentFragmentManager().popBackStack();
                                }
                            });
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
            return false;
        });
    }

    // Functional interface for lambda expression
    interface SearchQueryUpdater {
        void update(String query);
    }
}
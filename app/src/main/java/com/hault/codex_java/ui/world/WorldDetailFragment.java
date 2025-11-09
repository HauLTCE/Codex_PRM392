package com.hault.codex_java.ui.world;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hault.codex_java.R;
import com.hault.codex_java.viewmodel.CharacterViewModel;
import com.hault.codex_java.viewmodel.EventViewModel;
import com.hault.codex_java.viewmodel.LocationViewModel;
import com.hault.codex_java.viewmodel.WorldViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorldDetailFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private int worldId;

    private WorldViewModel worldViewModel;
    private CharacterViewModel characterViewModel;
    private LocationViewModel locationViewModel;
    private EventViewModel eventViewModel;

    private MenuItem characterSearchItem, locationSearchItem, eventSearchItem;
    private SearchView characterSearchView, locationSearchView, eventSearchView;

    public static WorldDetailFragment newInstance(int worldId) {
        WorldDetailFragment fragment = new WorldDetailFragment();
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
        worldViewModel = new ViewModelProvider(requireActivity()).get(WorldViewModel.class);
        characterViewModel = new ViewModelProvider(requireActivity()).get(CharacterViewModel.class);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
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
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        worldViewModel.getWorldById(worldId).observe(getViewLifecycleOwner(), world -> {
            if (world != null) {
                toolbar.setTitle(world.name);
            }
        });

        // Inflate all menus
        toolbar.inflateMenu(R.menu.world_detail_menu);
        toolbar.inflateMenu(R.menu.character_list_menu);
        toolbar.inflateMenu(R.menu.location_list_menu);
        toolbar.inflateMenu(R.menu.event_list_menu);

        setupToolbarMenuActions(toolbar);
        setupSearch(toolbar, viewPager);

        WorldDetailViewPagerAdapter adapter = new WorldDetailViewPagerAdapter(requireActivity(), worldId);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Characters");
                    break;
                case 1:
                    tab.setText("Locations");
                    break;
                case 2:
                    tab.setText("Events");
                    break;
            }
        }).attach();
    }

    private void setupSearch(MaterialToolbar toolbar, ViewPager2 viewPager) {
        characterSearchItem = toolbar.getMenu().findItem(R.id.action_search_characters);
        characterSearchView = (SearchView) characterSearchItem.getActionView();
        locationSearchItem = toolbar.getMenu().findItem(R.id.action_search_locations);
        locationSearchView = (SearchView) locationSearchItem.getActionView();
        eventSearchItem = toolbar.getMenu().findItem(R.id.action_search_events);
        eventSearchView = (SearchView) eventSearchItem.getActionView();

        characterSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }
            @Override public boolean onQueryTextChange(String newText) {
                characterViewModel.setSearchQuery(newText);
                return true;
            }
        });
        locationSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }
            @Override public boolean onQueryTextChange(String newText) {
                locationViewModel.setSearchQuery(newText);
                return true;
            }
        });
        eventSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }
            @Override public boolean onQueryTextChange(String newText) {
                eventViewModel.setSearchQuery(newText);
                return true;
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                characterSearchItem.setVisible(position == 0);
                locationSearchItem.setVisible(position == 1);
                eventSearchItem.setVisible(position == 2);

                if (!characterSearchView.isIconified()) characterSearchItem.collapseActionView();
                if (!locationSearchView.isIconified()) locationSearchItem.collapseActionView();
                if (!eventSearchView.isIconified()) eventSearchItem.collapseActionView();
            }
        });

        // Set initial visibility
        characterSearchItem.setVisible(true);
        locationSearchItem.setVisible(false);
        eventSearchItem.setVisible(false);
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
}
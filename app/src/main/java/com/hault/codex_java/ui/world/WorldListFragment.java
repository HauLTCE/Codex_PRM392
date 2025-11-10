package com.hault.codex_java.ui.world;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hault.codex_java.R;
import com.hault.codex_java.viewmodel.WorldViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorldListFragment extends Fragment {

    private WorldViewModel worldViewModel;
    private WorldListAdapter adapter;
    private TextInputEditText searchEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        worldViewModel = new ViewModelProvider(this).get(WorldViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_world_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        setupToolbar(toolbar);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWorlds);
        adapter = new WorldListAdapter();
        recyclerView.setAdapter(adapter);

        searchEditText = view.findViewById(R.id.search_edit_text);

        worldViewModel.getWorlds().observe(getViewLifecycleOwner(), worlds -> {
            adapter.submitList(worlds);
        });

        FloatingActionButton fab = view.findViewById(R.id.fabAddWorld);
        fab.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, new AddWorldFragment())
                    .addToBackStack(null)
                    .commit();
        });

        adapter.setOnItemClickListener(world -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, WorldDashboardFragment.newInstance(world.id))
                    .addToBackStack(null)
                    .commit();
        });

        setupSearch();
    }

    private void setupToolbar(MaterialToolbar toolbar) {
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.sort_asc) {
                worldViewModel.setSortOrder(WorldViewModel.SortOrder.ASC);
                return true;
            } else if (itemId == R.id.sort_desc) {
                worldViewModel.setSortOrder(WorldViewModel.SortOrder.DESC);
                return true;
            }
            return false;
        });
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                worldViewModel.setSearchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }
}
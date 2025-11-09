package com.hault.codex_java.ui.world;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.World;
import com.hault.codex_java.viewmodel.WorldViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorldListFragment extends Fragment {

    private WorldViewModel worldViewModel;
    private WorldListAdapter adapter;

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
                    .replace(R.id.fragment_container_view, WorldDetailFragment.newInstance(world.id))
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
                World worldToDelete = adapter.getWorldAt(position);
                worldViewModel.delete(worldToDelete);
                Snackbar.make(view, "World deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> worldViewModel.insert(worldToDelete))
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setupToolbar(MaterialToolbar toolbar) {
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sort_asc) {
                worldViewModel.setSortOrder(WorldViewModel.SortOrder.ASC);
                return true;
            } else if (item.getItemId() == R.id.sort_desc) {
                worldViewModel.setSortOrder(WorldViewModel.SortOrder.DESC);
                return true;
            }
            return false;
        });

        MenuItem searchItem = toolbar.getMenu().findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                worldViewModel.setSearchQuery(newText);
                return true;
            }
        });
    }
}
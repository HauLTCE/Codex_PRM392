package com.hault.codex_java.ui.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.viewmodel.LocationViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LocationListFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private int worldId;
    private LocationViewModel locationViewModel;
    private LocationListAdapter adapter;

    public static LocationListFragment newInstance(int worldId) {
        LocationListFragment fragment = new LocationListFragment();
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
        return inflater.inflate(R.layout.fragment_location_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        locationViewModel.setWorldId(worldId);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLocations);
        adapter = new LocationListAdapter();
        recyclerView.setAdapter(adapter);

        locationViewModel.getLocations().observe(getViewLifecycleOwner(), locations -> {
            adapter.submitList(locations);
        });

        FloatingActionButton fab = view.findViewById(R.id.fabAddLocation);
        fab.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, AddLocationFragment.newInstance(worldId))
                    .addToBackStack(null)
                    .commit();
        });

        adapter.setOnItemClickListener(location -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, EditLocationFragment.newInstance(worldId, location.id))
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
                Location locationToDelete = adapter.getLocationAt(position);
                locationViewModel.delete(locationToDelete);
                Snackbar.make(view, "Location deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> locationViewModel.insert(locationToDelete))
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}
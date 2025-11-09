package com.hault.codex_java.ui.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.hault.codex_java.R;
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

        adapter.setOnItemClickListener(location -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, LocationDetailFragment.newInstance(location.id))
                    .addToBackStack(null)
                    .commit();
        });
    }
}
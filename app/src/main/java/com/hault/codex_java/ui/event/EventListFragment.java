package com.hault.codex_java.ui.event;

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
import com.hault.codex_java.viewmodel.EventViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventListFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private int worldId;
    private EventViewModel eventViewModel;
    private EventListAdapter adapter;

    public static EventListFragment newInstance(int worldId) {
        EventListFragment fragment = new EventListFragment();
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
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        eventViewModel.setWorldId(worldId);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewEvents);
        adapter = new EventListAdapter();
        recyclerView.setAdapter(adapter);

        eventViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            adapter.submitList(events);
        });

        adapter.setOnItemClickListener(event -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, EventDetailFragment.newInstance(event.id))
                    .addToBackStack(null)
                    .commit();
        });
    }
}
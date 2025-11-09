package com.hault.codex_java.ui.arc;

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
import com.hault.codex_java.ui.chapter.ChapterListFragment;
import com.hault.codex_java.viewmodel.ArcViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ArcListFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private int worldId;
    private ArcViewModel arcViewModel;
    private ArcListAdapter adapter;

    public static ArcListFragment newInstance(int worldId) {
        ArcListFragment fragment = new ArcListFragment();
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
        return inflater.inflate(R.layout.fragment_arc_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arcViewModel = new ViewModelProvider(requireActivity()).get(ArcViewModel.class);
        arcViewModel.setWorldId(worldId);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewArcs);
        adapter = new ArcListAdapter();
        recyclerView.setAdapter(adapter);

        arcViewModel.getArcs().observe(getViewLifecycleOwner(), arcs -> adapter.submitList(arcs));

        adapter.setOnItemClickListener(arc -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, ArcDashboardFragment.newInstance(worldId, arc.id))
                    .addToBackStack(null)
                    .commit();
        });
    }
}
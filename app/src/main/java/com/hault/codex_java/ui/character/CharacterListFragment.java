package com.hault.codex_java.ui.character;

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
import com.hault.codex_java.viewmodel.CharacterViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CharacterListFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private int worldId;
    private CharacterViewModel characterViewModel;
    private CharacterListAdapter adapter;

    public static CharacterListFragment newInstance(int worldId) {
        CharacterListFragment fragment = new CharacterListFragment();
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
        return inflater.inflate(R.layout.fragment_character_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        characterViewModel = new ViewModelProvider(requireActivity()).get(CharacterViewModel.class);
        characterViewModel.setWorldId(worldId);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCharacters);
        adapter = new CharacterListAdapter();
        recyclerView.setAdapter(adapter);

        characterViewModel.getCharacters().observe(getViewLifecycleOwner(), characters -> {
            adapter.submitList(characters);
        });

        adapter.setOnItemClickListener(character -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, CharacterDetailFragment.newInstance(character.id))
                    .addToBackStack(null)
                    .commit();
        });
    }
}
package com.hault.codex_java.ui.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.viewmodel.EventViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEventFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private EventViewModel eventViewModel;
    private TextInputEditText editTextEventName;
    private TextInputEditText editTextEventDate;
    private TextInputEditText editTextEventDescription;
    private int worldId;

    public static AddEventFragment newInstance(int worldId) {
        AddEventFragment fragment = new AddEventFragment();
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
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEventName = view.findViewById(R.id.editTextEventName);
        editTextEventDate = view.findViewById(R.id.editTextEventDate);
        editTextEventDescription = view.findViewById(R.id.editTextEventDescription);
        Button buttonSaveEvent = view.findViewById(R.id.buttonSaveEvent);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Add Event");

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        buttonSaveEvent.setOnClickListener(v -> saveEvent());
    }

    private void saveEvent() {
        String name = editTextEventName.getText().toString().trim();
        String date = editTextEventDate.getText().toString().trim();
        String description = editTextEventDescription.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty()) {
            Toast.makeText(getContext(), "Event name and date are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Event newEvent = new Event(name, description, date, worldId);
        eventViewModel.insert(newEvent);
        getParentFragmentManager().popBackStack();
    }
}
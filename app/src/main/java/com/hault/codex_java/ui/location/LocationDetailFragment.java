package com.hault.codex_java.ui.location;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.data.model.crossref.EventLocationCrossRef;
import com.hault.codex_java.data.model.relations.LocationWithDetails;
import com.hault.codex_java.ui.character.CharacterListAdapter;
import com.hault.codex_java.ui.dialog.ItemSelectorDialogFragment;
import com.hault.codex_java.ui.event.EventListAdapter;
import com.hault.codex_java.viewmodel.CrossRefViewModel;
import com.hault.codex_java.viewmodel.EventViewModel;
import com.hault.codex_java.viewmodel.LocationViewModel;
import java.util.ArrayList;
import java.util.List;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LocationDetailFragment extends Fragment {

    private static final String ARG_LOCATION_ID = "location_id";
    private static final String REQUEST_KEY_SELECT_EVENTS = "requestKey_location_selectEvents";
    private int locationId;

    private LocationViewModel locationViewModel;
    private EventViewModel eventViewModel;
    private CrossRefViewModel crossRefViewModel;
    private LocationWithDetails currentLocationDetails;

    private MaterialToolbar toolbar;
    private ImageView imageViewHeader;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView tvDescription;
    private RecyclerView rvResidents, rvEvents;
    private CharacterListAdapter residentsAdapter;
    private EventListAdapter eventAdapter;
    private ImageButton btnAddEvent;

    public static LocationDetailFragment newInstance(int locationId) {
        LocationDetailFragment fragment = new LocationDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LOCATION_ID, locationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            locationId = getArguments().getInt(ARG_LOCATION_ID);
        }
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        crossRefViewModel = new ViewModelProvider(this).get(CrossRefViewModel.class);

        setupFragmentResultListeners();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupToolbar();
        setupRecyclerViews();
        setupClickListeners();

        locationViewModel.getLocationWithDetails(locationId).observe(getViewLifecycleOwner(), locationWithDetails -> {
            if (locationWithDetails != null) {
                this.currentLocationDetails = locationWithDetails;
                updateUI();
            }
        });
    }

    private void bindViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        imageViewHeader = view.findViewById(R.id.imageViewHeader);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) appBarLayout.getChildAt(0);
        tvDescription = view.findViewById(R.id.textViewDescription);
        rvResidents = view.findViewById(R.id.recyclerViewResidents);
        rvEvents = view.findViewById(R.id.recyclerViewEvents);
        btnAddEvent = view.findViewById(R.id.btnAddEvent);
    }

    private void setupRecyclerViews() {
        residentsAdapter = new CharacterListAdapter();
        rvResidents.setAdapter(residentsAdapter);

        eventAdapter = new EventListAdapter();
        rvEvents.setAdapter(eventAdapter);
    }

    private void updateUI() {
        toolbar.setTitle(currentLocationDetails.location.name);
        tvDescription.setText(currentLocationDetails.location.description);
        residentsAdapter.submitList(currentLocationDetails.residents);
        eventAdapter.submitList(currentLocationDetails.events);

        if (currentLocationDetails.location.imageUri != null && !currentLocationDetails.location.imageUri.isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(currentLocationDetails.location.imageUri))
                    .centerCrop()
                    .into(imageViewHeader);
        }

        if (currentLocationDetails.location.colorHex != null && !currentLocationDetails.location.colorHex.isEmpty()) {
            int color = Color.parseColor(currentLocationDetails.location.colorHex);
            collapsingToolbarLayout.setContentScrimColor(color);
            appBarLayout.setBackgroundColor(color);
        }
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_edit) {
                navigateToEdit();
                return true;
            } else if (itemId == R.id.action_delete) {
                confirmDelete();
                return true;
            }
            return false;
        });
    }

    private void setupClickListeners() {
        btnAddEvent.setOnClickListener(v -> openEventSelector());
    }

    private void openEventSelector() {
        if (currentLocationDetails == null) return;
        int worldId = currentLocationDetails.location.worldId;

        eventViewModel.getEventsForWorld(worldId).observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventViewModel.getEventsForWorld(worldId).removeObserver(this);
                ArrayList<String> names = new ArrayList<>();
                ArrayList<Integer> ids = new ArrayList<>();
                for(Event event : events) {
                    names.add(event.name);
                    ids.add(event.id);
                }
                ItemSelectorDialogFragment.newInstance("Link Events to " + currentLocationDetails.location.name, names, ids, REQUEST_KEY_SELECT_EVENTS)
                        .show(getParentFragmentManager(), ItemSelectorDialogFragment.TAG);
            }
        });
    }

    private void setupFragmentResultListeners() {
        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_SELECT_EVENTS, this, (requestKey, bundle) -> {
            ArrayList<Integer> selectedIds = bundle.getIntegerArrayList(ItemSelectorDialogFragment.RESULT_KEY_SELECTED_IDS);
            if (selectedIds != null) {
                for (Integer eventId : selectedIds) {
                    EventLocationCrossRef crossRef = new EventLocationCrossRef(eventId, locationId);
                    crossRefViewModel.insert(crossRef);
                }
            }
        });
    }

    private void navigateToEdit() {
        if (currentLocationDetails != null) {
            int worldId = currentLocationDetails.location.worldId;
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, EditLocationFragment.newInstance(worldId, locationId))
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void confirmDelete() {
        if (currentLocationDetails == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Location")
                .setMessage("Are you sure you want to delete '" + currentLocationDetails.location.name + "'? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    locationViewModel.delete(currentLocationDetails.location);
                    getParentFragmentManager().popBackStack();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
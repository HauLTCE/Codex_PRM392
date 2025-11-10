package com.hault.codex_java.ui.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.hault.codex_java.R;

import java.util.ArrayList;

public class ItemSelectorDialogFragment extends DialogFragment {

    public static final String TAG = "ItemSelectorDialog";
    public static final String RESULT_KEY_SELECTED_IDS = "selectedIds";

    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_ITEM_NAMES = "arg_item_names";
    private static final String ARG_ITEM_IDS = "arg_item_ids";
    private static final String ARG_REQUEST_KEY = "arg_request_key";

    private ItemSelectorAdapter adapter;

    public static ItemSelectorDialogFragment newInstance(String title, ArrayList<String> itemNames, ArrayList<Integer> itemIds, String requestKey) {
        ItemSelectorDialogFragment fragment = new ItemSelectorDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putStringArrayList(ARG_ITEM_NAMES, itemNames);
        args.putIntegerArrayList(ARG_ITEM_IDS, itemIds);
        args.putString(ARG_REQUEST_KEY, requestKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_item_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments().getString(ARG_TITLE);
        ArrayList<String> itemNames = getArguments().getStringArrayList(ARG_ITEM_NAMES);
        ArrayList<Integer> itemIds = getArguments().getIntegerArrayList(ARG_ITEM_IDS);
        String requestKey = getArguments().getString(ARG_REQUEST_KEY);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        TextInputEditText searchEditText = view.findViewById(R.id.search_edit_text);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewItems);
        Button btnCancel = view.findViewById(R.id.buttonCancel);
        Button btnSave = view.findViewById(R.id.buttonSave);

        toolbar.setTitle(title);
        adapter = new ItemSelectorAdapter(itemNames, itemIds);
        recyclerView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnCancel.setOnClickListener(v -> dismiss());
        btnSave.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putIntegerArrayList(RESULT_KEY_SELECTED_IDS, adapter.getSelectedIds());
            getParentFragmentManager().setFragmentResult(requestKey, result);
            dismiss();
        });
    }
}
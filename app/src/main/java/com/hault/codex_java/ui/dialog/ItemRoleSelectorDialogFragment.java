package com.hault.codex_java.ui.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hault.codex_java.R;

import java.util.ArrayList;

public class ItemRoleSelectorDialogFragment extends DialogFragment {

    public static final String TAG = "ItemRoleSelectorDialog";
    public static final String RESULT_KEY_SELECTED_IDS = "selectedIds";
    public static final String RESULT_KEY_ROLE = "role";

    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_ITEM_NAMES = "arg_item_names";
    private static final String ARG_ITEM_IDS = "arg_item_ids";
    private static final String ARG_REQUEST_KEY = "arg_request_key";
    private static final String ARG_ROLE_HINT = "arg_role_hint";

    private ItemSelectorAdapter adapter;
    private TextInputEditText editTextRole;
    private TextInputLayout roleInputLayout;
    private boolean isRoleRequired;

    public static ItemRoleSelectorDialogFragment newInstance(String title, ArrayList<String> itemNames, ArrayList<Integer> itemIds, String requestKey, @Nullable String roleHint) {
        ItemRoleSelectorDialogFragment fragment = new ItemRoleSelectorDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putStringArrayList(ARG_ITEM_NAMES, itemNames);
        args.putIntegerArrayList(ARG_ITEM_IDS, itemIds);
        args.putString(ARG_REQUEST_KEY, requestKey);
        args.putString(ARG_ROLE_HINT, roleHint);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_item_role_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments().getString(ARG_TITLE);
        ArrayList<String> itemNames = getArguments().getStringArrayList(ARG_ITEM_NAMES);
        ArrayList<Integer> itemIds = getArguments().getIntegerArrayList(ARG_ITEM_IDS);
        String requestKey = getArguments().getString(ARG_REQUEST_KEY);
        String roleHint = getArguments().getString(ARG_ROLE_HINT);
        isRoleRequired = roleHint != null;

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        TextInputEditText searchEditText = view.findViewById(R.id.search_edit_text);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewItems);
        roleInputLayout = view.findViewById(R.id.roleInputLayout);
        editTextRole = view.findViewById(R.id.editTextRole);
        Button btnCancel = view.findViewById(R.id.buttonCancel);
        Button btnSave = view.findViewById(R.id.buttonSave);

        toolbar.setTitle(title);
        adapter = new ItemSelectorAdapter(itemNames, itemIds);
        recyclerView.setAdapter(adapter);

        if (isRoleRequired) {
            roleInputLayout.setVisibility(View.VISIBLE);
            roleInputLayout.setHint(roleHint);
        } else {
            roleInputLayout.setVisibility(View.GONE);
        }

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
            ArrayList<Integer> selectedIds = adapter.getSelectedIds();
            if (selectedIds.isEmpty()) {
                Toast.makeText(getContext(), "Please select at least one item.", Toast.LENGTH_SHORT).show();
                return;
            }

            String role = editTextRole.getText().toString().trim();
            if (isRoleRequired && role.isEmpty()) {
                Toast.makeText(getContext(), roleInputLayout.getHint() + " cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle result = new Bundle();
            result.putIntegerArrayList(RESULT_KEY_SELECTED_IDS, selectedIds);
            if (isRoleRequired) {
                result.putString(RESULT_KEY_ROLE, role);
            }
            getParentFragmentManager().setFragmentResult(requestKey, result);
            dismiss();
        });
    }
}
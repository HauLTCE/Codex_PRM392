package com.hault.codex_java.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.hault.codex_java.R;
import java.util.ArrayList;

public class RelationshipBuilderDialogFragment extends DialogFragment {

    public static final String TAG = "RelationshipBuilderDialog";

    public static final String BUNDLE_KEY_ACTION = "action";
    public static final String BUNDLE_KEY_SOURCE_ID = "sourceId";
    public static final String BUNDLE_KEY_TARGET_ID = "targetId";
    public static final String BUNDLE_KEY_DESCRIPTION = "description";

    public static final String ACTION_SAVE = "save";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";

    private static final String ARG_IS_EDIT_MODE = "arg_is_edit_mode";
    private static final String ARG_SOURCE_CHAR_ID = "arg_source_char_id";
    private static final String ARG_BASE_CHAR_NAME = "arg_base_char_name";
    private static final String ARG_TARGET_CHAR_NAMES = "arg_target_char_names";
    private static final String ARG_TARGET_CHAR_IDS = "arg_target_char_ids";
    private static final String ARG_EXISTING_TARGET_ID = "arg_existing_target_id";
    private static final String ARG_EXISTING_DESC = "arg_existing_desc";
    private static final String ARG_REQUEST_KEY = "arg_request_key";

    private Spinner spinnerTargetCharacter;
    private TextInputEditText editTextDescription;
    private ArrayList<Integer> targetCharacterIds;
    private boolean isEditMode = false;
    private int sourceCharacterId;
    private int existingTargetId;


    public static RelationshipBuilderDialogFragment newInstanceForAdd(int sourceCharId, String baseCharacterName, ArrayList<String> targetCharacterNames, ArrayList<Integer> targetCharacterIds, String requestKey) {
        RelationshipBuilderDialogFragment fragment = new RelationshipBuilderDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_EDIT_MODE, false);
        args.putInt(ARG_SOURCE_CHAR_ID, sourceCharId);
        args.putString(ARG_BASE_CHAR_NAME, baseCharacterName);
        args.putStringArrayList(ARG_TARGET_CHAR_NAMES, targetCharacterNames);
        args.putIntegerArrayList(ARG_TARGET_CHAR_IDS, targetCharacterIds);
        args.putString(ARG_REQUEST_KEY, requestKey);
        fragment.setArguments(args);
        return fragment;
    }

    public static RelationshipBuilderDialogFragment newInstanceForEdit(int sourceCharId, String baseCharacterName, String targetCharacterName, int targetCharacterId, String existingDescription, String requestKey) {
        RelationshipBuilderDialogFragment fragment = new RelationshipBuilderDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_EDIT_MODE, true);
        args.putInt(ARG_SOURCE_CHAR_ID, sourceCharId);
        args.putString(ARG_BASE_CHAR_NAME, baseCharacterName);
        ArrayList<String> names = new ArrayList<>();
        names.add(targetCharacterName);
        args.putStringArrayList(ARG_TARGET_CHAR_NAMES, names);
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(targetCharacterId);
        args.putIntegerArrayList(ARG_TARGET_CHAR_IDS, ids);
        args.putInt(ARG_EXISTING_TARGET_ID, targetCharacterId);
        args.putString(ARG_EXISTING_DESC, existingDescription);
        args.putString(ARG_REQUEST_KEY, requestKey);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_relationship_builder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Common Args
        isEditMode = getArguments().getBoolean(ARG_IS_EDIT_MODE);
        sourceCharacterId = getArguments().getInt(ARG_SOURCE_CHAR_ID);
        String baseCharName = getArguments().getString(ARG_BASE_CHAR_NAME);
        ArrayList<String> targetCharacterNames = getArguments().getStringArrayList(ARG_TARGET_CHAR_NAMES);
        targetCharacterIds = getArguments().getIntegerArrayList(ARG_TARGET_CHAR_IDS);
        String requestKey = getArguments().getString(ARG_REQUEST_KEY);

        // View Binding
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        TextView tvPrompt = view.findViewById(R.id.textViewRelationshipPrompt);
        spinnerTargetCharacter = view.findViewById(R.id.spinnerTargetCharacter);
        editTextDescription = view.findViewById(R.id.editTextRelationshipDescription);
        Button btnDelete = view.findViewById(R.id.buttonDelete);
        Button btnCancel = view.findViewById(R.id.buttonCancel);
        Button btnSave = view.findViewById(R.id.buttonSave);

        // Common Setup
        tvPrompt.setText(baseCharName + " is the...");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, targetCharacterNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTargetCharacter.setAdapter(adapter);
        btnCancel.setOnClickListener(v -> dismiss());

        // Mode-Specific Setup
        if (isEditMode) {
            existingTargetId = getArguments().getInt(ARG_EXISTING_TARGET_ID);
            String existingDesc = getArguments().getString(ARG_EXISTING_DESC);

            toolbar.setTitle("Edit Relationship");
            spinnerTargetCharacter.setEnabled(false);
            editTextDescription.setText(existingDesc);
            btnSave.setText("Update");
            btnDelete.setVisibility(View.VISIBLE);

            btnSave.setOnClickListener(v -> sendResult(ACTION_UPDATE, existingTargetId, requestKey));
            btnDelete.setOnClickListener(v -> sendResult(ACTION_DELETE, existingTargetId, requestKey));

        } else { // Add Mode
            toolbar.setTitle("Add Relationship");
            btnSave.setText("Save");
            btnDelete.setVisibility(View.GONE);
            btnSave.setOnClickListener(v -> {
                int selectedPosition = spinnerTargetCharacter.getSelectedItemPosition();
                if (selectedPosition < 0 || selectedPosition >= targetCharacterIds.size()) {
                    Toast.makeText(getContext(), "Please select a character", Toast.LENGTH_SHORT).show();
                    return;
                }
                int targetId = targetCharacterIds.get(selectedPosition);
                sendResult(ACTION_SAVE, targetId, requestKey);
            });
        }
    }

    private void sendResult(String action, int targetId, String requestKey) {
        String description = editTextDescription.getText().toString().trim();

        if (!action.equals(ACTION_DELETE) && description.isEmpty()) {
            Toast.makeText(getContext(), "Relationship description cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle result = new Bundle();
        result.putString(BUNDLE_KEY_ACTION, action);
        result.putInt(BUNDLE_KEY_SOURCE_ID, sourceCharacterId);
        result.putInt(BUNDLE_KEY_TARGET_ID, targetId);
        if (!action.equals(ACTION_DELETE)) {
            result.putString(BUNDLE_KEY_DESCRIPTION, description);
        }

        getParentFragmentManager().setFragmentResult(requestKey, result);
        dismiss();
    }
}
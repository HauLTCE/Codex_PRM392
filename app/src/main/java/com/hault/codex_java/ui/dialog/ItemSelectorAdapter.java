package com.hault.codex_java.ui.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.hault.codex_java.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemSelectorAdapter extends RecyclerView.Adapter<ItemSelectorAdapter.ItemViewHolder> {

    private final List<SelectableItem> allItems = new ArrayList<>();
    private List<SelectableItem> filteredItems = new ArrayList<>();
    private final Map<Integer, Boolean> selectedState = new HashMap<>();

    public ItemSelectorAdapter(List<String> names, List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
            allItems.add(new SelectableItem(names.get(i), ids.get(i)));
        }
        this.filteredItems = new ArrayList<>(allItems);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selector_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        SelectableItem item = filteredItems.get(position);
        holder.checkBox.setText(item.name);
        holder.checkBox.setChecked(selectedState.getOrDefault(item.id, false));
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedState.put(item.id, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    public void filter(String query) {
        filteredItems.clear();
        if (query.isEmpty()) {
            filteredItems.addAll(allItems);
        } else {
            for (SelectableItem item : allItems) {
                if (item.name.toLowerCase().contains(query.toLowerCase())) {
                    filteredItems.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getSelectedIds() {
        ArrayList<Integer> selectedIds = new ArrayList<>();
        for (Map.Entry<Integer, Boolean> entry : selectedState.entrySet()) {
            if (entry.getValue()) {
                selectedIds.add(entry.getKey());
            }
        }
        return selectedIds;
    }

    static class SelectableItem {
        final String name;
        final int id;
        SelectableItem(String name, int id) {
            this.name = name;
            this.id = id;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        final MaterialCheckBox checkBox;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_item);
        }
    }
}
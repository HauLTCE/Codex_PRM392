package com.hault.codex_java.ui.character;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.crossref.CharacterCharacterCrossRef;

public class RelationshipListAdapter extends ListAdapter<RelationshipListAdapter.RelationshipDisplayItem, RelationshipListAdapter.RelationshipViewHolder> {

    private OnItemClickListener listener;

    public static class RelationshipDisplayItem {
        public final CharacterCharacterCrossRef crossRef;
        public final com.hault.codex_java.data.model.Character targetCharacter;

        public RelationshipDisplayItem(CharacterCharacterCrossRef crossRef, com.hault.codex_java.data.model.Character targetCharacter) {
            this.crossRef = crossRef;
            this.targetCharacter = targetCharacter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RelationshipDisplayItem that = (RelationshipDisplayItem) o;
            return crossRef.characterOneId == that.crossRef.characterOneId &&
                    crossRef.characterTwoId == that.crossRef.characterTwoId;
        }
    }

    public RelationshipListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<RelationshipDisplayItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<RelationshipDisplayItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull RelationshipDisplayItem oldItem, @NonNull RelationshipDisplayItem newItem) {
            return oldItem.crossRef.characterOneId == newItem.crossRef.characterOneId &&
                    oldItem.crossRef.characterTwoId == newItem.crossRef.characterTwoId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull RelationshipDisplayItem oldItem, @NonNull RelationshipDisplayItem newItem) {
            return oldItem.targetCharacter.name.equals(newItem.targetCharacter.name) &&
                    oldItem.crossRef.relationshipDescription.equals(newItem.crossRef.relationshipDescription);
        }
    };


    @NonNull
    @Override
    public RelationshipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.relationship_list_item, parent, false);
        return new RelationshipViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RelationshipViewHolder holder, int position) {
        RelationshipDisplayItem currentItem = getItem(position);
        if (currentItem != null && currentItem.targetCharacter != null) {
            holder.textViewRelationship.setText(currentItem.crossRef.relationshipDescription);
            holder.textViewRelatedCharacterName.setText(currentItem.targetCharacter.name);
        }
    }

    class RelationshipViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewRelationship;
        private final TextView textViewRelatedCharacterName;

        public RelationshipViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRelationship = itemView.findViewById(R.id.textViewRelationship);
            textViewRelatedCharacterName = itemView.findViewById(R.id.textViewRelatedCharacterName);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RelationshipDisplayItem item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
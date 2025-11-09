package com.hault.codex_java.ui.character;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.crossref.CharacterCharacterCrossRef;
import com.hault.codex_java.data.model.relations.CharacterWithDetails;

import java.util.ArrayList;
import java.util.List;

public class RelationshipListAdapter extends RecyclerView.Adapter<RelationshipListAdapter.RelationshipViewHolder> {

    private List<CharacterCharacterCrossRef> relationships = new ArrayList<>();
    private List<com.hault.codex_java.data.model.Character> relatedCharacters = new ArrayList<>();

    @NonNull
    @Override
    public RelationshipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.relationship_list_item, parent, false);
        return new RelationshipViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RelationshipViewHolder holder, int position) {
        CharacterCharacterCrossRef currentRelationship = relationships.get(position);
        com.hault.codex_java.data.model.Character relatedCharacter = findCharacterById(currentRelationship.characterTwoId);

        if (relatedCharacter != null) {
            holder.textViewRelationship.setText(currentRelationship.relationshipDescription);
            holder.textViewRelatedCharacterName.setText(relatedCharacter.name);
        }
    }

    @Override
    public int getItemCount() {
        return relationships.size();
    }

    public void setRelationships(List<CharacterCharacterCrossRef> relationships, List<com.hault.codex_java.data.model.Character> relatedCharacters) {
        this.relationships = relationships;
        this.relatedCharacters = relatedCharacters;
        notifyDataSetChanged();
    }

    private com.hault.codex_java.data.model.Character findCharacterById(int id) {
        for (com.hault.codex_java.data.model.Character character : relatedCharacters) {
            if (character.id == id) {
                return character;
            }
        }
        return null;
    }

    static class RelationshipViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewRelationship;
        private final TextView textViewRelatedCharacterName;

        public RelationshipViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRelationship = itemView.findViewById(R.id.textViewRelationship);
            textViewRelatedCharacterName = itemView.findViewById(R.id.textViewRelatedCharacterName);
        }
    }
}
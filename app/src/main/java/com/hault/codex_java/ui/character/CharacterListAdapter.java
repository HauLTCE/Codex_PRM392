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
import com.hault.codex_java.data.model.Character;

public class CharacterListAdapter extends ListAdapter<Character, CharacterListAdapter.CharacterViewHolder> {

    private OnItemClickListener listener;

    public CharacterListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Character> DIFF_CALLBACK = new DiffUtil.ItemCallback<Character>() {
        @Override
        public boolean areItemsTheSame(@NonNull Character oldItem, @NonNull Character newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Character oldItem, @NonNull Character newItem) {
            return oldItem.name.equals(newItem.name) &&
                    oldItem.backstory.equals(newItem.backstory);
        }
    };

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.character_list_item, parent, false);
        return new CharacterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        Character currentCharacter = getItem(position);
        holder.textViewCharacterName.setText(currentCharacter.name);
        holder.textViewCharacterBackstory.setText(currentCharacter.backstory);
    }

    public Character getCharacterAt(int position) {
        return getItem(position);
    }

    class CharacterViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewCharacterName;
        private final TextView textViewCharacterBackstory;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCharacterName = itemView.findViewById(R.id.textViewCharacterName);
            textViewCharacterBackstory = itemView.findViewById(R.id.textViewCharacterBackstory);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Character character);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
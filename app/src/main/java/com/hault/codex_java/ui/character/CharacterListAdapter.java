package com.hault.codex_java.ui.character;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
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
                    oldItem.backstory.equals(newItem.backstory) &&
                    oldItem.isPinned == newItem.isPinned &&
                    (oldItem.imageUri == null ? newItem.imageUri == null : oldItem.imageUri.equals(newItem.imageUri)) &&
                    (oldItem.colorHex == null ? newItem.colorHex == null : oldItem.colorHex.equals(newItem.colorHex));
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
        holder.imageViewPin.setVisibility(currentCharacter.isPinned ? View.VISIBLE : View.GONE);

        if (currentCharacter.imageUri != null && !currentCharacter.imageUri.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(currentCharacter.imageUri))
                    .centerCrop()
                    .into(holder.imageViewItem);
        } else {
            // Set a placeholder or clear the image
            Glide.with(holder.itemView.getContext()).clear(holder.imageViewItem);
            holder.imageViewItem.setImageResource(0); // Or a placeholder drawable
        }

        if (currentCharacter.colorHex != null && !currentCharacter.colorHex.isEmpty()) {
            holder.colorStripe.setBackgroundColor(Color.parseColor(currentCharacter.colorHex));
            holder.colorStripe.setVisibility(View.VISIBLE);
        } else {
            holder.colorStripe.setVisibility(View.GONE);
        }
    }

    public Character getCharacterAt(int position) {
        return getItem(position);
    }

    class CharacterViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewCharacterName;
        private final TextView textViewCharacterBackstory;
        private final ImageView imageViewPin;
        private final ImageView imageViewItem;
        private final View colorStripe;


        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCharacterName = itemView.findViewById(R.id.textViewCharacterName);
            textViewCharacterBackstory = itemView.findViewById(R.id.textViewCharacterBackstory);
            imageViewPin = itemView.findViewById(R.id.imageViewPin);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            colorStripe = itemView.findViewById(R.id.colorStripe);


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
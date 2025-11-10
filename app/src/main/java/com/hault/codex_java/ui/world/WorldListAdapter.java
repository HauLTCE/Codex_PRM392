package com.hault.codex_java.ui.world;

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
import com.hault.codex_java.data.model.World;

public class WorldListAdapter extends ListAdapter<World, WorldListAdapter.WorldViewHolder> {

    private OnItemClickListener listener;

    public WorldListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<World> DIFF_CALLBACK = new DiffUtil.ItemCallback<World>() {
        @Override
        public boolean areItemsTheSame(@NonNull World oldItem, @NonNull World newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull World oldItem, @NonNull World newItem) {
            return oldItem.name.equals(newItem.name) &&
                    oldItem.description.equals(newItem.description) &&
                    oldItem.isPinned == newItem.isPinned &&
                    (oldItem.imageUri == null ? newItem.imageUri == null : oldItem.imageUri.equals(newItem.imageUri)) &&
                    (oldItem.colorHex == null ? newItem.colorHex == null : oldItem.colorHex.equals(newItem.colorHex));
        }
    };

    @NonNull
    @Override
    public WorldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.world_list_item, parent, false);
        return new WorldViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorldViewHolder holder, int position) {
        World currentWorld = getItem(position);
        holder.textViewWorldName.setText(currentWorld.name);
        holder.textViewWorldDescription.setText(currentWorld.description);
        holder.imageViewPin.setVisibility(currentWorld.isPinned ? View.VISIBLE : View.GONE);

        if (currentWorld.imageUri != null && !currentWorld.imageUri.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(currentWorld.imageUri))
                    .centerCrop()
                    .into(holder.imageViewItem);
        } else {
            holder.imageViewItem.setImageResource(0); // Clear image
        }

        if (currentWorld.colorHex != null && !currentWorld.colorHex.isEmpty()) {
            holder.colorStripe.setBackgroundColor(Color.parseColor(currentWorld.colorHex));
            holder.colorStripe.setVisibility(View.VISIBLE);
        } else {
            holder.colorStripe.setVisibility(View.GONE);
        }
    }

    public World getWorldAt(int position) {
        return getItem(position);
    }

    class WorldViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewWorldName;
        private final TextView textViewWorldDescription;
        private final ImageView imageViewPin;
        private final ImageView imageViewItem;
        private final View colorStripe;

        public WorldViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWorldName = itemView.findViewById(R.id.textViewWorldName);
            textViewWorldDescription = itemView.findViewById(R.id.textViewWorldDescription);
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
        void onItemClick(World world);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
package com.hault.codex_java.ui.world;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
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
                    oldItem.description.equals(newItem.description);
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
    }

    public World getWorldAt(int position) {
        return getItem(position);
    }

    class WorldViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewWorldName;
        private final TextView textViewWorldDescription;

        public WorldViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWorldName = itemView.findViewById(R.id.textViewWorldName);
            textViewWorldDescription = itemView.findViewById(R.id.textViewWorldDescription);

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
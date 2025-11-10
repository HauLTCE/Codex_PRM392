package com.hault.codex_java.ui.location;

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
import com.hault.codex_java.data.model.Location;

public class LocationListAdapter extends ListAdapter<Location, LocationListAdapter.LocationViewHolder> {

    private OnItemClickListener listener;

    public LocationListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Location> DIFF_CALLBACK = new DiffUtil.ItemCallback<Location>() {
        @Override
        public boolean areItemsTheSame(@NonNull Location oldItem, @NonNull Location newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Location oldItem, @NonNull Location newItem) {
            return oldItem.name.equals(newItem.name) &&
                    oldItem.description.equals(newItem.description) &&
                    oldItem.isPinned == newItem.isPinned &&
                    (oldItem.imageUri == null ? newItem.imageUri == null : oldItem.imageUri.equals(newItem.imageUri)) &&
                    (oldItem.colorHex == null ? newItem.colorHex == null : oldItem.colorHex.equals(newItem.colorHex));
        }
    };

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_list_item, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location currentLocation = getItem(position);
        holder.textViewLocationName.setText(currentLocation.name);
        holder.textViewLocationDescription.setText(currentLocation.description);
        holder.imageViewPin.setVisibility(currentLocation.isPinned ? View.VISIBLE : View.GONE);

        if (currentLocation.imageUri != null && !currentLocation.imageUri.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(currentLocation.imageUri))
                    .centerCrop()
                    .into(holder.imageViewItem);
        } else {
            Glide.with(holder.itemView.getContext()).clear(holder.imageViewItem);
            holder.imageViewItem.setImageResource(0);
        }

        if (currentLocation.colorHex != null && !currentLocation.colorHex.isEmpty()) {
            holder.colorStripe.setBackgroundColor(Color.parseColor(currentLocation.colorHex));
            holder.colorStripe.setVisibility(View.VISIBLE);
        } else {
            holder.colorStripe.setVisibility(View.GONE);
        }
    }

    public Location getLocationAt(int position) {
        return getItem(position);
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewLocationName;
        private final TextView textViewLocationDescription;
        private final ImageView imageViewPin;
        private final ImageView imageViewItem;
        private final View colorStripe;


        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLocationName = itemView.findViewById(R.id.textViewLocationName);
            textViewLocationDescription = itemView.findViewById(R.id.textViewLocationDescription);
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
        void onItemClick(Location location);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
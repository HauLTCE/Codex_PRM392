package com.hault.codex_java.ui.event;

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
import com.hault.codex_java.data.model.Event;

public class EventListAdapter extends ListAdapter<Event, EventListAdapter.EventViewHolder> {

    private OnItemClickListener listener;

    public EventListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Event> DIFF_CALLBACK = new DiffUtil.ItemCallback<Event>() {
        @Override
        public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.name.equals(newItem.name) &&
                    oldItem.date.equals(newItem.date) &&
                    oldItem.description.equals(newItem.description) &&
                    oldItem.isPinned == newItem.isPinned &&
                    (oldItem.imageUri == null ? newItem.imageUri == null : oldItem.imageUri.equals(newItem.imageUri)) &&
                    (oldItem.colorHex == null ? newItem.colorHex == null : oldItem.colorHex.equals(newItem.colorHex));
        }
    };

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event currentEvent = getItem(position);
        holder.textViewEventName.setText(currentEvent.name);
        holder.textViewEventDate.setText(currentEvent.date);
        holder.textViewEventDescription.setText(currentEvent.description);
        holder.imageViewPin.setVisibility(currentEvent.isPinned ? View.VISIBLE : View.GONE);

        if (currentEvent.imageUri != null && !currentEvent.imageUri.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(currentEvent.imageUri))
                    .centerCrop()
                    .into(holder.imageViewItem);
        } else {
            Glide.with(holder.itemView.getContext()).clear(holder.imageViewItem);
            holder.imageViewItem.setImageResource(0);
        }

        if (currentEvent.colorHex != null && !currentEvent.colorHex.isEmpty()) {
            holder.colorStripe.setBackgroundColor(Color.parseColor(currentEvent.colorHex));
            holder.colorStripe.setVisibility(View.VISIBLE);
        } else {
            holder.colorStripe.setVisibility(View.GONE);
        }
    }

    public Event getEventAt(int position) {
        return getItem(position);
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewEventName;
        private final TextView textViewEventDate;
        private final TextView textViewEventDescription;
        private final ImageView imageViewPin;
        private final ImageView imageViewItem;
        private final View colorStripe;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEventName = itemView.findViewById(R.id.textViewEventName);
            textViewEventDate = itemView.findViewById(R.id.textViewEventDate);
            textViewEventDescription = itemView.findViewById(R.id.textViewEventDescription);
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
        void onItemClick(Event event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
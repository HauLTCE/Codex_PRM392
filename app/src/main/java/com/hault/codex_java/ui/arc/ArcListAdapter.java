package com.hault.codex_java.ui.arc;

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
import com.hault.codex_java.data.model.Arc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArcListAdapter extends ListAdapter<Arc, ArcListAdapter.ArcViewHolder> {

    private OnItemClickListener listener;
    private OnOrderChangedListener orderListener;

    public ArcListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Arc> DIFF_CALLBACK = new DiffUtil.ItemCallback<Arc>() {
        @Override
        public boolean areItemsTheSame(@NonNull Arc oldItem, @NonNull Arc newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Arc oldItem, @NonNull Arc newItem) {
            return oldItem.title.equals(newItem.title) &&
                    oldItem.synopsis.equals(newItem.synopsis) &&
                    oldItem.isPinned == newItem.isPinned &&
                    oldItem.displayOrder == newItem.displayOrder &&
                    (oldItem.imageUri == null ? newItem.imageUri == null : oldItem.imageUri.equals(newItem.imageUri)) &&
                    (oldItem.colorHex == null ? newItem.colorHex == null : oldItem.colorHex.equals(newItem.colorHex));
        }
    };

    @NonNull
    @Override
    public ArcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.arc_list_item, parent, false);
        return new ArcViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArcViewHolder holder, int position) {
        Arc currentArc = getItem(position);
        holder.textViewArcTitle.setText(currentArc.title);
        holder.textViewArcSynopsis.setText(currentArc.synopsis);
        holder.imageViewPin.setVisibility(currentArc.isPinned ? View.VISIBLE : View.GONE);

        if (currentArc.imageUri != null && !currentArc.imageUri.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(currentArc.imageUri))
                    .centerCrop()
                    .into(holder.imageViewItem);
        } else {
            Glide.with(holder.itemView.getContext()).clear(holder.imageViewItem);
            holder.imageViewItem.setImageResource(0);
        }

        if (currentArc.colorHex != null && !currentArc.colorHex.isEmpty()) {
            holder.colorStripe.setBackgroundColor(Color.parseColor(currentArc.colorHex));
            holder.colorStripe.setVisibility(View.VISIBLE);
        } else {
            holder.colorStripe.setVisibility(View.GONE);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        List<Arc> currentList = new ArrayList<>(getCurrentList());
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(currentList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(currentList, i, i - 1);
            }
        }
        submitList(currentList);
        // Do not call notifyItemMoved, as submitList handles updates.
    }

    public void onOrderFinished() {
        List<Arc> finalList = new ArrayList<>(getCurrentList());
        for(int i = 0; i < finalList.size(); i++) {
            finalList.get(i).displayOrder = i;
        }
        if (orderListener != null) {
            orderListener.onOrderChanged(finalList);
        }
    }

    public Arc getArcAt(int position) {
        return getItem(position);
    }

    class ArcViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewArcTitle;
        private final TextView textViewArcSynopsis;
        private final ImageView imageViewPin;
        private final ImageView imageViewItem;
        private final View colorStripe;


        public ArcViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewArcTitle = itemView.findViewById(R.id.textViewArcTitle);
            textViewArcSynopsis = itemView.findViewById(R.id.textViewArcSynopsis);
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
        void onItemClick(Arc arc);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnOrderChangedListener {
        void onOrderChanged(List<Arc> updatedList);
    }

    public void setOnOrderChangedListener(OnOrderChangedListener listener) {
        this.orderListener = listener;
    }
}
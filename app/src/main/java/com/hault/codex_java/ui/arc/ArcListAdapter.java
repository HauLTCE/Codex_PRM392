package com.hault.codex_java.ui.arc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Arc;

public class ArcListAdapter extends ListAdapter<Arc, ArcListAdapter.ArcViewHolder> {

    private OnItemClickListener listener;

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
                    oldItem.synopsis.equals(newItem.synopsis);
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
    }

    public Arc getArcAt(int position) {
        return getItem(position);
    }

    class ArcViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewArcTitle;
        private final TextView textViewArcSynopsis;

        public ArcViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewArcTitle = itemView.findViewById(R.id.textViewArcTitle);
            textViewArcSynopsis = itemView.findViewById(R.id.textViewArcSynopsis);

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
}
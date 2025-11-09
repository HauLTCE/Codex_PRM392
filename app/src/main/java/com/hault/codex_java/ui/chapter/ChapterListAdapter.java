package com.hault.codex_java.ui.chapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Chapter;

public class ChapterListAdapter extends ListAdapter<Chapter, ChapterListAdapter.ChapterViewHolder> {

    private OnItemClickListener listener;

    public ChapterListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Chapter> DIFF_CALLBACK = new DiffUtil.ItemCallback<Chapter>() {
        @Override
        public boolean areItemsTheSame(@NonNull Chapter oldItem, @NonNull Chapter newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Chapter oldItem, @NonNull Chapter newItem) {
            return oldItem.title.equals(newItem.title) &&
                    oldItem.chapterNumber == newItem.chapterNumber &&
                    oldItem.prose.equals(newItem.prose);
        }
    };

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_list_item, parent, false);
        return new ChapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter currentChapter = getItem(position);
        holder.textViewChapterTitle.setText(currentChapter.title);
        holder.textViewChapterNumber.setText("Chapter " + currentChapter.chapterNumber);
    }

    public Chapter getChapterAt(int position) {
        return getItem(position);
    }

    class ChapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewChapterTitle;
        private final TextView textViewChapterNumber;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewChapterTitle = itemView.findViewById(R.id.textViewChapterTitle);
            textViewChapterNumber = itemView.findViewById(R.id.textViewChapterNumber);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Chapter chapter);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
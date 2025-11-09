package com.hault.codex_java.data.model.crossref;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.hault.codex_java.data.model.Chapter;
import com.hault.codex_java.data.model.Event;

@Entity(
        tableName = "chapter_event_cross_ref",
        primaryKeys = {"chapterId", "eventId"},
        foreignKeys = {
                @ForeignKey(
                        entity = Chapter.class,
                        parentColumns = "id",
                        childColumns = "chapterId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Event.class,
                        parentColumns = "id",
                        childColumns = "eventId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("chapterId"),
                @Index("eventId")
        }
)
public class ChapterEventCrossRef {
    public int chapterId;
    public int eventId;

    public ChapterEventCrossRef(int chapterId, int eventId) {
        this.chapterId = chapterId;
        this.eventId = eventId;
    }
}
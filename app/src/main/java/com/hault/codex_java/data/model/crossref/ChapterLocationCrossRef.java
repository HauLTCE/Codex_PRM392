package com.hault.codex_java.data.model.crossref;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.hault.codex_java.data.model.Chapter;
import com.hault.codex_java.data.model.Location;

@Entity(
        tableName = "chapter_location_cross_ref",
        primaryKeys = {"chapterId", "locationId"},
        foreignKeys = {
                @ForeignKey(
                        entity = Chapter.class,
                        parentColumns = "id",
                        childColumns = "chapterId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Location.class,
                        parentColumns = "id",
                        childColumns = "locationId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("chapterId"),
                @Index("locationId")
        }
)
public class ChapterLocationCrossRef {
    public int chapterId;
    public int locationId;
    public String roleInChapter;

    public ChapterLocationCrossRef(int chapterId, int locationId, String roleInChapter) {
        this.chapterId = chapterId;
        this.locationId = locationId;
        this.roleInChapter = roleInChapter;
    }
}
package com.hault.codex_java.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "chapters",
        foreignKeys = {
                @ForeignKey(
                        entity = World.class,
                        parentColumns = "id",
                        childColumns = "worldId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Arc.class,
                        parentColumns = "id",
                        childColumns = "arcId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("worldId"),
                @Index("arcId")
        }
)
public class Chapter {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int arcId;
    public int worldId;
    public String title;
    public int chapterNumber;
    public String prose;
    public int wordCount;
    public long createdAt;
    public long lastModifiedAt;
    public String imageUri;
    public String tags;
    public boolean isPinned;
    public String colorHex;

    public Chapter(int arcId, int worldId, String title, int chapterNumber, String prose) {
        this.arcId = arcId;
        this.worldId = worldId;
        this.title = title;
        this.chapterNumber = chapterNumber;
        this.prose = prose;
        this.createdAt = System.currentTimeMillis();
        this.lastModifiedAt = System.currentTimeMillis();
        this.isPinned = false;
    }
}
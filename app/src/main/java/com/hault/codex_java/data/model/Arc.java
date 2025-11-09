package com.hault.codex_java.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "arcs",
        foreignKeys = @ForeignKey(
                entity = World.class,
                parentColumns = "id",
                childColumns = "worldId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("worldId")}
)
public class Arc {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int worldId;
    public String title;
    public String synopsis;
    public int displayOrder;
    public long createdAt;
    public long lastModifiedAt;
    public String imageUri;
    public String tags;
    public boolean isPinned;
    public String colorHex;

    public Arc(int worldId, String title, String synopsis, int displayOrder) {
        this.worldId = worldId;
        this.title = title;
        this.synopsis = synopsis;
        this.displayOrder = displayOrder;
        this.createdAt = System.currentTimeMillis();
        this.lastModifiedAt = System.currentTimeMillis();
        this.isPinned = false;
    }
}
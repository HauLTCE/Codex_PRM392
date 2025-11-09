package com.hault.codex_java.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "events",
        foreignKeys = @ForeignKey(
                entity = World.class,
                parentColumns = "id",
                childColumns = "worldId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("worldId")}
)
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String description;
    public String date;
    public int worldId;

    public Event(String name, String description, String date, int worldId) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.worldId = worldId;
    }
}

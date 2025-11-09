package com.hault.codex_java.data.model.crossref;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.data.model.Location;

@Entity(
        tableName = "event_location_cross_ref",
        primaryKeys = {"eventId", "locationId"},
        foreignKeys = {
                @ForeignKey(
                        entity = Event.class,
                        parentColumns = "id",
                        childColumns = "eventId",
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
                @Index("eventId"),
                @Index("locationId")
        }
)
public class EventLocationCrossRef {
    public int eventId;
    public int locationId;

    public EventLocationCrossRef(int eventId, int locationId) {
        this.eventId = eventId;
        this.locationId = locationId;
    }
}
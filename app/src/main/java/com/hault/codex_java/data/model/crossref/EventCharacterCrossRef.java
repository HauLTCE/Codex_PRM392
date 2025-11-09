package com.hault.codex_java.data.model.crossref;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.Event;

@Entity(
        tableName = "event_character_cross_ref",
        primaryKeys = {"eventId", "characterId"},
        foreignKeys = {
                @ForeignKey(
                        entity = Event.class,
                        parentColumns = "id",
                        childColumns = "eventId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Character.class,
                        parentColumns = "id",
                        childColumns = "characterId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("eventId"),
                @Index("characterId")
        }
)
public class EventCharacterCrossRef {
    public int eventId;
    public int characterId;
    public String roleInEvent;

    public EventCharacterCrossRef(int eventId, int characterId, String roleInEvent) {
        this.eventId = eventId;
        this.characterId = characterId;
        this.roleInEvent = roleInEvent;
    }
}
package com.hault.codex_java.data.model.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.Chapter;
import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.data.model.crossref.ChapterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterEventCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterLocationCrossRef;

import java.util.List;

public class ChapterWithDetails {
    @Embedded
    public Chapter chapter;

    @Relation(
            parentColumn = "id",
            entity = Character.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = ChapterCharacterCrossRef.class,
                    parentColumn = "chapterId",
                    entityColumn = "characterId"
            )
    )
    public List<Character> characters;

    @Relation(
            parentColumn = "id",
            entity = Location.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = ChapterLocationCrossRef.class,
                    parentColumn = "chapterId",
                    entityColumn = "locationId"
            )
    )
    public List<Location> locations;

    @Relation(
            parentColumn = "id",
            entity = Event.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = ChapterEventCrossRef.class,
                    parentColumn = "chapterId",
                    entityColumn = "eventId"
            )
    )
    public List<Event> events;
}
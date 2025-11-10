package com.hault.codex_java.data.model.crossref;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.hault.codex_java.data.model.Chapter;
import com.hault.codex_java.data.model.Character;

@Entity(
        tableName = "chapter_character_cross_ref",
        primaryKeys = {"chapterId", "characterId"},
        foreignKeys = {
                @ForeignKey(
                        entity = Chapter.class,
                        parentColumns = "id",
                        childColumns = "chapterId",
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
                @Index("chapterId"),
                @Index("characterId")
        }
)
public class ChapterCharacterCrossRef {
    public int chapterId;
    public int characterId;
    public String roleInChapter;

    public ChapterCharacterCrossRef(int chapterId, int characterId, String roleInChapter) {
        this.chapterId = chapterId;
        this.characterId = characterId;
        this.roleInChapter = roleInChapter;
    }
}
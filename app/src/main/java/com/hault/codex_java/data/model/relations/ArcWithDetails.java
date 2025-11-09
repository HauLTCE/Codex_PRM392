package com.hault.codex_java.data.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.hault.codex_java.data.model.Arc;
import com.hault.codex_java.data.model.Chapter;

import java.util.List;

public class ArcWithDetails {
    @Embedded
    public Arc arc;

    @Relation(parentColumn = "id", entityColumn = "arcId")
    public List<Chapter> chapters;
}
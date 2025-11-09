package com.hault.codex_java.di;

import android.content.Context;
import androidx.room.Room;

import com.hault.codex_java.data.local.dao.ArcDao;
import com.hault.codex_java.data.local.dao.ChapterDao;
import com.hault.codex_java.data.local.dao.CharacterDao;
import com.hault.codex_java.data.local.dao.CrossRefDao;
import com.hault.codex_java.data.local.dao.EventDao;
import com.hault.codex_java.data.local.dao.LocationDao;
import com.hault.codex_java.data.local.dao.WorldDao;
import com.hault.codex_java.data.local.db.AppDatabase;
import com.hault.codex_java.data.repository.ArcRepository;
import com.hault.codex_java.data.repository.ChapterRepository;
import com.hault.codex_java.data.repository.CharacterRepository;
import com.hault.codex_java.data.repository.CrossRefRepository;
import com.hault.codex_java.data.repository.EventRepository;
import com.hault.codex_java.data.repository.LocationRepository;
import com.hault.codex_java.data.repository.WorldRepository;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;


@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public static AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                "codex_database"
        ).addMigrations(
                AppDatabase.MIGRATION_2_3,
                AppDatabase.MIGRATION_3_4,
                AppDatabase.MIGRATION_4_5
        ).build();
    }

    @Provides
    public static WorldDao provideWorldDao(AppDatabase appDatabase) {
        return appDatabase.worldDao();
    }

    @Provides
    public static CharacterDao provideCharacterDao(AppDatabase appDatabase) {
        return appDatabase.characterDao();
    }

    @Provides
    public static LocationDao provideLocationDao(AppDatabase appDatabase) {
        return appDatabase.locationDao();
    }

    @Provides
    public static EventDao provideEventDao(AppDatabase appDatabase) {
        return appDatabase.eventDao();
    }

    @Provides
    public static ArcDao provideArcDao(AppDatabase appDatabase) {
        return appDatabase.arcDao();
    }

    @Provides
    public static ChapterDao provideChapterDao(AppDatabase appDatabase) {
        return appDatabase.chapterDao();
    }

    @Provides
    public static CrossRefDao provideCrossRefDao(AppDatabase appDatabase) {
        return appDatabase.crossRefDao();
    }

    @Provides
    @Singleton
    public static WorldRepository provideWorldRepository(WorldDao worldDao) {
        return new WorldRepository(worldDao);
    }

    @Provides
    @Singleton
    public static CharacterRepository provideCharacterRepository(CharacterDao characterDao) {
        return new CharacterRepository(characterDao);
    }

    @Provides
    @Singleton
    public static LocationRepository provideLocationRepository(LocationDao locationDao) {
        return new LocationRepository(locationDao);
    }

    @Provides
    @Singleton
    public static EventRepository provideEventRepository(EventDao eventDao) {
        return new EventRepository(eventDao);
    }

    @Provides
    @Singleton
    public static ArcRepository provideArcRepository(ArcDao arcDao) {
        return new ArcRepository(arcDao);
    }

    @Provides
    @Singleton
    public static ChapterRepository provideChapterRepository(ChapterDao chapterDao) {
        return new ChapterRepository(chapterDao);
    }

    @Provides
    @Singleton
    public static CrossRefRepository provideCrossRefRepository(CrossRefDao crossRefDao) {
        return new CrossRefRepository(crossRefDao);
    }
}
package com.username.codex

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.username.codex.data.local.WorldDao
import com.username.codex.data.model.World
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var worldDao: WorldDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun insertWorldAndReadIt() = runBlocking {
        val world = World(name = "Test World", description = "A world for testing")
        worldDao.insert(world)

        val worlds = worldDao.getAllWorlds().first()
        assertEquals("Test World", worlds.find { it.name == "Test World" }?.name)
    }
}

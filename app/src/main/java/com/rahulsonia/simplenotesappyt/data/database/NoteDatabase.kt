package com.rahulsonia.simplenotesappyt.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rahulsonia.simplenotesappyt.data.dao.NoteDao
import com.rahulsonia.simplenotesappyt.data.entity.Note

@Database(entities = arrayOf(Note::class), version = 1)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
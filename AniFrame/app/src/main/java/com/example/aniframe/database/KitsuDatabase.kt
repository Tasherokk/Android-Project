package com.example.aniframe.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KitsuDB::class], version = 1)
abstract class kitsuDatabase : RoomDatabase() {
    abstract fun kitsuDao(): KitsuDao
}
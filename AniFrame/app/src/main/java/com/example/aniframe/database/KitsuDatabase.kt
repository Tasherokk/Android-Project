package com.example.aniframe.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [KitsuDB::class], version = 1)
abstract class KitsuDatabase : RoomDatabase() {
    abstract fun kitsuDao(): KitsuDao
}
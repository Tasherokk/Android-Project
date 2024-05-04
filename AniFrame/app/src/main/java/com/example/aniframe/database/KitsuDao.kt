package com.example.aniframe.database

import androidx.room.*

@Dao
interface KitsuDao {
    @Query("SELECT * FROM kitsudb")
      fun getAll(): List<KitsuDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(kitsuList: List<KitsuDB>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(kitsu: KitsuDB)
    @Delete
    fun delete(kitsu: KitsuDB)
}
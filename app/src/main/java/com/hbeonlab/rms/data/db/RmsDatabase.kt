package com.hbeonlab.rms.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hbeonlab.rms.data.dao.RmsDao
import com.hbeonlab.rms.data.models.RmsData

@Database(
    entities = [RmsData::class],
    version =  7
)
abstract class RmsDatabase : RoomDatabase() {
    abstract fun getRmsDao():RmsDao
}
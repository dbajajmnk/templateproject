package com.hbeonlab.rms.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hbeonlab.rms.data.models.RmsData

@Dao
interface RmsDao {

    @Insert
    suspend fun insertRmsData(rmsData: RmsData)

    @Query("Select * from RmsData")
    fun getAllRmsData() : LiveData<List<RmsData>>

    @Delete
    suspend fun deleteRmsData(rmsData: RmsData)
}
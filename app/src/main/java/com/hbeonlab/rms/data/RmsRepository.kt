package com.hbeonlab.rms.data

import androidx.lifecycle.LiveData
import com.hbeonlab.rms.data.dao.RmsDao
import com.hbeonlab.rms.data.models.RmsData

class RmsRepository(val rmsDao: RmsDao) {

    suspend fun getRmsData(): LiveData<List<RmsData>> {
        return rmsDao.getAllRmsData()
    }
}
package com.hbeonlab.rms.utils

import com.hbeonlab.rms.data.models.RmsData

object DataGenerator {
    fun getData():List<RmsData>{
        val dataList : MutableList<RmsData> = mutableListOf()
        for(i in 1..20){
            val rmsData = RmsData(
                null,"imei$i",i,i,"Pump error$i",i.toDouble(),i.toDouble(),i,i.toDouble(),i,
                "PhaseCurentRYB$i","Longitude$i","Latitude$i",i,i,i,i,i,"DateTime$i",
                i,i,i,i,"CreatedOnDate$i")
            dataList.add(rmsData)
        }
        return dataList
    }
}
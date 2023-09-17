package com.hbeonlab.rms.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RmsData")
data class RmsData(
    @PrimaryKey(autoGenerate = true)
    val sNo: Int? = null,
    val iMEINo: String,
    val powerStatus: Int,// '1'
    val pumpStatus: Int,// '1-On, 2-Off',
    val pumpError: String, // DEFAULT '1' COMMENT '1-No Error, else Error',
    val voltage: Double,
    val current: Double,
    val frequency: Int,
    val temperature: Double,
    val power: Int,
    val phaseCurrentRYB: String,
    val longitude: String,
    val latitude: String,
    val gSMSignal: Int,
    val digitalinput1: Int,
    val digitalinput2: Int,
    val analoginput1: Int,
    val analoginput2: Int,
    val logDateTime: String,// DEFAULT CURRENT_TIMESTAMP,
    val manualCommand: Int,// DEFAULT '3',
    val resetType: Int,// DEFAULT '0',
    val resetCounter: Int,// DEFAULT '0',
    val controllerModel: Int,// DEFAULT '1',
    val createdOnDate: String,// DEFAULT CURRENT_TIMESTAMP
) {
    object RmsConverter {
        fun convert(stringData: String): RmsData {
            val datas = stringData.split(",")
            return RmsData(
                iMEINo = datas[0],
                powerStatus = datas[1].toInt(),
                pumpStatus = datas[2].toInt(),
                pumpError = datas[3],
                voltage = datas[4].toDouble(),
                current = datas[5].toDouble(),
                frequency = datas[6].toInt(),
                temperature = datas[7].toDouble(),
                power = datas[8].toInt(),
                phaseCurrentRYB = datas[9],
                longitude = datas[10],
                latitude = datas[11],
                gSMSignal = datas[12].toInt(),
                digitalinput1 = datas[13].toInt(),
                digitalinput2 = datas[14].toInt(),
                analoginput1 = datas[15].toInt(),
                analoginput2 = datas[16].toInt(),
                logDateTime = datas[17],
                manualCommand = datas[18].toInt(),
                resetType = datas[19].toInt(),
                resetCounter = datas[20].toInt(),
                controllerModel = datas[21].toInt(),
                createdOnDate = datas[22]
            )
        }
    }
}
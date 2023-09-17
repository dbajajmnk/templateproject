package com.hbeonlab.rms.di

import androidx.room.Room
import com.hbeonlab.rms.data.RmsRepository
import com.hbeonlab.rms.data.db.RmsDatabase
import com.hbeonlab.rms.vm.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    single { RmsRepository(get()) }
    fun provideDao(rmsDatabase: RmsDatabase) = rmsDatabase.getRmsDao()

    single {
        Room.databaseBuilder(androidApplication(), RmsDatabase::class.java, "rms_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        provideDao(get())
    }

}
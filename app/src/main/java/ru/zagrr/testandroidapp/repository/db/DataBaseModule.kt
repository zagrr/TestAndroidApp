package ru.zagrr.testandroidapp.repository.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataBaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context : Context) : TestRoomDatabase {
        return Room
            .databaseBuilder(context, TestRoomDatabase::class.java, "test_database")
            .build()
    }

    @Singleton
    @Provides
    fun provideDocumentDao(database: TestRoomDatabase) : DocumentDao {
        return database.documentDao()
    }
}
package com.rmldev.georgianalphabet.database

import android.content.Context
import androidx.room.Room
import com.rmldev.georgianalphabet.feature.alphabet.db.AlphabetFeatureDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DatabaseModule {

    @Binds
    fun bindAlphabetDb(db: AppDatabase): AlphabetFeatureDatabase

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "ge_alph.db").build()
    }
}

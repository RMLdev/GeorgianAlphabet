package com.rmldev.georgianalphabet.core.audio.di

import android.content.Context
import android.media.AudioManager
import com.rmldev.georgianalphabet.core.audio.MediaPlayerPlaybackFactory
import com.rmldev.georgianalphabet.core.audio.SoundPlayerImpl
import com.rmldev.georgianalphabet.core.audio.SystemAudioFocus
import com.rmldev.georgianalphabet.core.audio.api.SoundPlayer
import com.rmldev.georgianalphabet.core.coroutines.di.Dispatcher
import com.rmldev.georgianalphabet.core.coroutines.di.GeAlphDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AudioModule {

    @Provides
    @Singleton
    fun providesSoundPlayer(
        @ApplicationContext context: Context,
        @Dispatcher(GeAlphDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): SoundPlayer = SoundPlayerImpl(
        playbackFactory = MediaPlayerPlaybackFactory(context.assets),
        audioFocus = SystemAudioFocus(context.getSystemService(AudioManager::class.java)),
        ioDispatcher = ioDispatcher,
    )
}

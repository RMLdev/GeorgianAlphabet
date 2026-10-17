package com.rmldev.georgianalphabet.feature.alphabet.ui.di

import androidx.navigation3.runtime.EntryProviderScope
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import com.rmldev.georgianalphabet.core.navigation.api.Navigator
import com.rmldev.georgianalphabet.core.navigation.api.TabKey
import com.rmldev.georgianalphabet.feature.alphabet.api.AlphabetNavKey
import com.rmldev.georgianalphabet.feature.alphabet.ui.navigation.GlyphDetailsNavKey
import com.rmldev.georgianalphabet.feature.alphabet.ui.navigation.provideEntries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Module
@InstallIn(ActivityRetainedComponent::class)
interface AlphabetUiFeatureModule {

    companion object {

        @IntoSet
        @Provides
        fun provideEntryBuilder(): EntryProviderScope<NavigationKey>.(Navigator) -> Unit = {
            provideEntries(it)
        }

        @IntoSet
        @Provides
        fun provideKeySerializerRegistrar(): @JvmSuppressWildcards SerializersModuleBuilder.() -> Unit = {
            polymorphic(NavigationKey::class) {
                subclass(AlphabetNavKey::class)
                subclass(GlyphDetailsNavKey::class)
            }
            polymorphic(TabKey::class) {
                subclass(AlphabetNavKey::class)
            }
        }
    }
}
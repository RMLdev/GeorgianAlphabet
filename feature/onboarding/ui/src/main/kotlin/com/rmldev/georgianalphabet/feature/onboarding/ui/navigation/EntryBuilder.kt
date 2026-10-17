package com.rmldev.georgianalphabet.feature.onboarding.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import com.rmldev.georgianalphabet.core.navigation.api.Navigator
import com.rmldev.georgianalphabet.feature.alphabet.api.AlphabetNavKey
import com.rmldev.georgianalphabet.feature.onboarding.api.OnboardingNavKey
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.OnboardingRoute

fun EntryProviderScope<NavigationKey>.provideEntries(navigator: Navigator) {
    entry<OnboardingNavKey> {
        // Переход на таб закрывает overlay вместе с онбордингом, «Назад» из алфавита его не вернёт
        OnboardingRoute(onFinished = { navigator.navigateTo(AlphabetNavKey) })
    }
}

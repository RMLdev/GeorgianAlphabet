package com.rmldev.georgianalphabet.core.navigation.api

interface NavigationContext {
    val currentTab: TabKey
    val currentBackStack: List<NavigationKey>
    fun getStackByTab(tabKey: TabKey): List<NavigationKey>?
}
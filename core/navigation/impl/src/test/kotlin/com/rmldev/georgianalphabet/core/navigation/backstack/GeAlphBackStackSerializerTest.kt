package com.rmldev.georgianalphabet.core.navigation.backstack

import com.rmldev.georgianalphabet.core.navigation.BlockingScreen
import com.rmldev.georgianalphabet.core.navigation.Details
import com.rmldev.georgianalphabet.core.navigation.HomeTab
import com.rmldev.georgianalphabet.core.navigation.ModalScreen
import com.rmldev.georgianalphabet.core.navigation.Settings
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GeAlphBackStackSerializerTest {

    private val serializer = GeAlphBackStackSerializer(PolymorphicSerializer(NavigationKey::class))

    private val json = Json {
        serializersModule = SerializersModule {
            polymorphic(NavigationKey::class) {
                subclass(HomeTab::class)
                subclass(Details::class)
                subclass(Settings::class)
                subclass(ModalScreen::class)
                subclass(BlockingScreen::class)
            }
        }
    }

    @Test
    fun `round trip keeps keys, order and entry ids`() {
        val stack = GeAlphBackStack<NavigationKey>(HomeTab, Details(1), Settings, ModalScreen(7), BlockingScreen)

        val restored = json.decodeFromString(serializer, json.encodeToString(serializer, stack))

        assertEquals(stack.toList(), restored.toList())
    }

    @Test
    fun `stable serial names are used in saved state`() {
        val encoded = json.encodeToString(serializer, GeAlphBackStack<NavigationKey>(HomeTab, Details(3)))

        assert("\"HomeTab\"" in encoded) { encoded }
        assert("\"Details\"" in encoded) { encoded }
    }

    @Test
    fun `unregistered key fails on save`() {
        val json = Json {
            serializersModule = SerializersModule {
                polymorphic(NavigationKey::class) { subclass(HomeTab::class) }
            }
        }

        assertFailsWith<SerializationException> {
            json.encodeToString(serializer, GeAlphBackStack<NavigationKey>(HomeTab, Details(1)))
        }
    }
}

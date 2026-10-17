package com.rmldev.georgianalphabet.core.navigation.backstack

import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import com.rmldev.georgianalphabet.core.navigation.api.BackStackEntry
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class DjsBackStackSerializer<T : NavigationKey>(elementSerializer: KSerializer<T>) :
    KSerializer<DjcBackStack<T>> {

    private val delegate = SnapshotStateListSerializer(BackStackEntry.serializer(elementSerializer))

    override val descriptor: SerialDescriptor =
        SerialDescriptor(
            "com.rmldev.georgianalphabet.core.navigation.DjcBackStack",
            delegate.descriptor
        )

    override fun serialize(encoder: Encoder, value: DjcBackStack<T>) {
        encoder.encodeSerializableValue(serializer = delegate, value = value.base)
    }

    override fun deserialize(decoder: Decoder): DjcBackStack<T> {
        return DjcBackStack(base = decoder.decodeSerializableValue(deserializer = delegate))
    }
}
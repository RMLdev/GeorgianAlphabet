package com.rmldev.georgianalphabet.feature.alphabet.ui.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlinx.serialization.modules.SerializersModuleBuilder;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class AlphabetUiFeatureModule_Companion_ProvideKeySerializerRegistrarFactory implements Factory<Function1<SerializersModuleBuilder, Unit>> {
  @Override
  public Function1<SerializersModuleBuilder, Unit> get() {
    return provideKeySerializerRegistrar();
  }

  public static AlphabetUiFeatureModule_Companion_ProvideKeySerializerRegistrarFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static Function1<SerializersModuleBuilder, Unit> provideKeySerializerRegistrar() {
    return Preconditions.checkNotNullFromProvides(AlphabetUiFeatureModule.Companion.provideKeySerializerRegistrar());
  }

  private static final class InstanceHolder {
    static final AlphabetUiFeatureModule_Companion_ProvideKeySerializerRegistrarFactory INSTANCE = new AlphabetUiFeatureModule_Companion_ProvideKeySerializerRegistrarFactory();
  }
}

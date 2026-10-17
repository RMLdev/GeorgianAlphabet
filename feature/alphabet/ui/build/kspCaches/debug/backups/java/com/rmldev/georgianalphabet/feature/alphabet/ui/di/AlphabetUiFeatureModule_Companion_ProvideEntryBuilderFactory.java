package com.rmldev.georgianalphabet.feature.alphabet.ui.di;

import androidx.navigation3.runtime.EntryProviderScope;
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey;
import com.rmldev.georgianalphabet.core.navigation.api.Navigator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

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
public final class AlphabetUiFeatureModule_Companion_ProvideEntryBuilderFactory implements Factory<Function2<EntryProviderScope<NavigationKey>, Navigator, Unit>> {
  @Override
  public Function2<EntryProviderScope<NavigationKey>, Navigator, Unit> get() {
    return provideEntryBuilder();
  }

  public static AlphabetUiFeatureModule_Companion_ProvideEntryBuilderFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static Function2<EntryProviderScope<NavigationKey>, Navigator, Unit> provideEntryBuilder(
      ) {
    return Preconditions.checkNotNullFromProvides(AlphabetUiFeatureModule.Companion.provideEntryBuilder());
  }

  private static final class InstanceHolder {
    static final AlphabetUiFeatureModule_Companion_ProvideEntryBuilderFactory INSTANCE = new AlphabetUiFeatureModule_Companion_ProvideEntryBuilderFactory();
  }
}

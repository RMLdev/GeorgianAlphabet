package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class AlphabetStateToUiMapper_Factory implements Factory<AlphabetStateToUiMapper> {
  @Override
  public AlphabetStateToUiMapper get() {
    return newInstance();
  }

  public static AlphabetStateToUiMapper_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AlphabetStateToUiMapper newInstance() {
    return new AlphabetStateToUiMapper();
  }

  private static final class InstanceHolder {
    static final AlphabetStateToUiMapper_Factory INSTANCE = new AlphabetStateToUiMapper_Factory();
  }
}

package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store;

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
public final class AlphabetReducer_Factory implements Factory<AlphabetReducer> {
  @Override
  public AlphabetReducer get() {
    return newInstance();
  }

  public static AlphabetReducer_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AlphabetReducer newInstance() {
    return new AlphabetReducer();
  }

  private static final class InstanceHolder {
    static final AlphabetReducer_Factory INSTANCE = new AlphabetReducer_Factory();
  }
}

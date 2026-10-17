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
public final class AlphabetActor_Factory implements Factory<AlphabetActor> {
  @Override
  public AlphabetActor get() {
    return newInstance();
  }

  public static AlphabetActor_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AlphabetActor newInstance() {
    return new AlphabetActor();
  }

  private static final class InstanceHolder {
    static final AlphabetActor_Factory INSTANCE = new AlphabetActor_Factory();
  }
}

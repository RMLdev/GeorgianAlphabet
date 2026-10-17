package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store;

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
public final class GlyphDetailsActor_Factory implements Factory<GlyphDetailsActor> {
  @Override
  public GlyphDetailsActor get() {
    return newInstance();
  }

  public static GlyphDetailsActor_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GlyphDetailsActor newInstance() {
    return new GlyphDetailsActor();
  }

  private static final class InstanceHolder {
    static final GlyphDetailsActor_Factory INSTANCE = new GlyphDetailsActor_Factory();
  }
}

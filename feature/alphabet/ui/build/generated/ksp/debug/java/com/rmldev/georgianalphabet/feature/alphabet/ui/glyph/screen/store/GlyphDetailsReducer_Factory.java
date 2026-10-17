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
public final class GlyphDetailsReducer_Factory implements Factory<GlyphDetailsReducer> {
  @Override
  public GlyphDetailsReducer get() {
    return newInstance();
  }

  public static GlyphDetailsReducer_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GlyphDetailsReducer newInstance() {
    return new GlyphDetailsReducer();
  }

  private static final class InstanceHolder {
    static final GlyphDetailsReducer_Factory INSTANCE = new GlyphDetailsReducer_Factory();
  }
}

package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state;

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
public final class GlyphDetailsStateToUiMapper_Factory implements Factory<GlyphDetailsStateToUiMapper> {
  @Override
  public GlyphDetailsStateToUiMapper get() {
    return newInstance();
  }

  public static GlyphDetailsStateToUiMapper_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GlyphDetailsStateToUiMapper newInstance() {
    return new GlyphDetailsStateToUiMapper();
  }

  private static final class InstanceHolder {
    static final GlyphDetailsStateToUiMapper_Factory INSTANCE = new GlyphDetailsStateToUiMapper_Factory();
  }
}

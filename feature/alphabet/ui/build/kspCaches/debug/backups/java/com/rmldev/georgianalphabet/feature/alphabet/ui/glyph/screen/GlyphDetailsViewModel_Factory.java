package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen;

import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.GlyphDetailsActor;
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.GlyphDetailsReducer;
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsStateToUiMapper;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
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
public final class GlyphDetailsViewModel_Factory implements Factory<GlyphDetailsViewModel> {
  private final Provider<GlyphDetailsActor> actorProvider;

  private final Provider<GlyphDetailsReducer> reducerProvider;

  private final Provider<GlyphDetailsStateToUiMapper> uiStateMapperProvider;

  private GlyphDetailsViewModel_Factory(Provider<GlyphDetailsActor> actorProvider,
      Provider<GlyphDetailsReducer> reducerProvider,
      Provider<GlyphDetailsStateToUiMapper> uiStateMapperProvider) {
    this.actorProvider = actorProvider;
    this.reducerProvider = reducerProvider;
    this.uiStateMapperProvider = uiStateMapperProvider;
  }

  @Override
  public GlyphDetailsViewModel get() {
    return newInstance(actorProvider.get(), reducerProvider.get(), uiStateMapperProvider.get());
  }

  public static GlyphDetailsViewModel_Factory create(Provider<GlyphDetailsActor> actorProvider,
      Provider<GlyphDetailsReducer> reducerProvider,
      Provider<GlyphDetailsStateToUiMapper> uiStateMapperProvider) {
    return new GlyphDetailsViewModel_Factory(actorProvider, reducerProvider, uiStateMapperProvider);
  }

  public static GlyphDetailsViewModel newInstance(GlyphDetailsActor actor,
      GlyphDetailsReducer reducer, GlyphDetailsStateToUiMapper uiStateMapper) {
    return new GlyphDetailsViewModel(actor, reducer, uiStateMapper);
  }
}

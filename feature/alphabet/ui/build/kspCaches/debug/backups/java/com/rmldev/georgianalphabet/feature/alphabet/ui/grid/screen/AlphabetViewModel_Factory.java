package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen;

import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.AlphabetActor;
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.AlphabetReducer;
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetStateToUiMapper;
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
public final class AlphabetViewModel_Factory implements Factory<AlphabetViewModel> {
  private final Provider<AlphabetActor> actorProvider;

  private final Provider<AlphabetReducer> reducerProvider;

  private final Provider<AlphabetStateToUiMapper> uiStateMapperProvider;

  private AlphabetViewModel_Factory(Provider<AlphabetActor> actorProvider,
      Provider<AlphabetReducer> reducerProvider,
      Provider<AlphabetStateToUiMapper> uiStateMapperProvider) {
    this.actorProvider = actorProvider;
    this.reducerProvider = reducerProvider;
    this.uiStateMapperProvider = uiStateMapperProvider;
  }

  @Override
  public AlphabetViewModel get() {
    return newInstance(actorProvider.get(), reducerProvider.get(), uiStateMapperProvider.get());
  }

  public static AlphabetViewModel_Factory create(Provider<AlphabetActor> actorProvider,
      Provider<AlphabetReducer> reducerProvider,
      Provider<AlphabetStateToUiMapper> uiStateMapperProvider) {
    return new AlphabetViewModel_Factory(actorProvider, reducerProvider, uiStateMapperProvider);
  }

  public static AlphabetViewModel newInstance(AlphabetActor actor, AlphabetReducer reducer,
      AlphabetStateToUiMapper uiStateMapper) {
    return new AlphabetViewModel(actor, reducer, uiStateMapper);
  }
}

package com.rmldev.georgianalphabet.core.datastore.di;

import android.content.Context;
import com.rmldev.georgianalphabet.core.datastore.AeadSerializerCreator;
import com.rmldev.georgianalphabet.core.datastore.api.DataStoreProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineScope;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata({
    "dagger.hilt.android.qualifiers.ApplicationContext",
    "com.rmldev.georgianalphabet.core.coroutines.di.ApplicationScope",
    "com.rmldev.georgianalphabet.core.coroutines.di.Dispatcher"
})
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
public final class DataStoreModule_ProvidesDataStoreProviderFactory implements Factory<DataStoreProvider> {
  private final Provider<Context> applicationContextProvider;

  private final Provider<AeadSerializerCreator> aeadSerializerCreatorProvider;

  private final Provider<CoroutineScope> scopeProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  private DataStoreModule_ProvidesDataStoreProviderFactory(
      Provider<Context> applicationContextProvider,
      Provider<AeadSerializerCreator> aeadSerializerCreatorProvider,
      Provider<CoroutineScope> scopeProvider, Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.applicationContextProvider = applicationContextProvider;
    this.aeadSerializerCreatorProvider = aeadSerializerCreatorProvider;
    this.scopeProvider = scopeProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public DataStoreProvider get() {
    return providesDataStoreProvider(applicationContextProvider.get(), aeadSerializerCreatorProvider.get(), scopeProvider.get(), ioDispatcherProvider.get());
  }

  public static DataStoreModule_ProvidesDataStoreProviderFactory create(
      Provider<Context> applicationContextProvider,
      Provider<AeadSerializerCreator> aeadSerializerCreatorProvider,
      Provider<CoroutineScope> scopeProvider, Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new DataStoreModule_ProvidesDataStoreProviderFactory(applicationContextProvider, aeadSerializerCreatorProvider, scopeProvider, ioDispatcherProvider);
  }

  public static DataStoreProvider providesDataStoreProvider(Context applicationContext,
      AeadSerializerCreator aeadSerializerCreator, CoroutineScope scope,
      CoroutineDispatcher ioDispatcher) {
    return Preconditions.checkNotNullFromProvides(DataStoreModule.INSTANCE.providesDataStoreProvider(applicationContext, aeadSerializerCreator, scope, ioDispatcher));
  }
}

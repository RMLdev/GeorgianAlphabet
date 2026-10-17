package com.rmldev.georgianalphabet.core.network.impl;

import dagger.Lazy;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("com.rmldev.georgianalphabet.core.network.api.Unauthenticated")
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
public final class NetworkModule_Companion_ProvidesUnauthenticatedOkHttpClientFactory implements Factory<OkHttpClient> {
  private final Provider<OkHttpClient> baseProvider;

  private NetworkModule_Companion_ProvidesUnauthenticatedOkHttpClientFactory(
      Provider<OkHttpClient> baseProvider) {
    this.baseProvider = baseProvider;
  }

  @Override
  public OkHttpClient get() {
    return providesUnauthenticatedOkHttpClient(DoubleCheck.lazy(baseProvider));
  }

  public static NetworkModule_Companion_ProvidesUnauthenticatedOkHttpClientFactory create(
      Provider<OkHttpClient> baseProvider) {
    return new NetworkModule_Companion_ProvidesUnauthenticatedOkHttpClientFactory(baseProvider);
  }

  public static OkHttpClient providesUnauthenticatedOkHttpClient(Lazy<OkHttpClient> base) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.Companion.providesUnauthenticatedOkHttpClient(base));
  }
}

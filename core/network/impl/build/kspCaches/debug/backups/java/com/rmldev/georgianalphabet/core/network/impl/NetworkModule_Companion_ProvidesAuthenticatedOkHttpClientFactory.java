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
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("com.rmldev.georgianalphabet.core.network.api.Authenticated")
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
public final class NetworkModule_Companion_ProvidesAuthenticatedOkHttpClientFactory implements Factory<OkHttpClient> {
  private final Provider<OkHttpClient> baseProvider;

  private final Provider<AuthHeaderInterceptor> authHeaderInterceptorProvider;

  private final Provider<Authenticator> authenticatorProvider;

  private NetworkModule_Companion_ProvidesAuthenticatedOkHttpClientFactory(
      Provider<OkHttpClient> baseProvider,
      Provider<AuthHeaderInterceptor> authHeaderInterceptorProvider,
      Provider<Authenticator> authenticatorProvider) {
    this.baseProvider = baseProvider;
    this.authHeaderInterceptorProvider = authHeaderInterceptorProvider;
    this.authenticatorProvider = authenticatorProvider;
  }

  @Override
  public OkHttpClient get() {
    return providesAuthenticatedOkHttpClient(DoubleCheck.lazy(baseProvider), authHeaderInterceptorProvider.get(), authenticatorProvider.get());
  }

  public static NetworkModule_Companion_ProvidesAuthenticatedOkHttpClientFactory create(
      Provider<OkHttpClient> baseProvider,
      Provider<AuthHeaderInterceptor> authHeaderInterceptorProvider,
      Provider<Authenticator> authenticatorProvider) {
    return new NetworkModule_Companion_ProvidesAuthenticatedOkHttpClientFactory(baseProvider, authHeaderInterceptorProvider, authenticatorProvider);
  }

  public static OkHttpClient providesAuthenticatedOkHttpClient(Lazy<OkHttpClient> base,
      AuthHeaderInterceptor authHeaderInterceptor, Authenticator authenticator) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.Companion.providesAuthenticatedOkHttpClient(base, authHeaderInterceptor, authenticator));
  }
}

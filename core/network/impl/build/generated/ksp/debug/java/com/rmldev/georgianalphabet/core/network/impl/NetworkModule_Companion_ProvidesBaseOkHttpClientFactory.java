package com.rmldev.georgianalphabet.core.network.impl;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_Companion_ProvidesBaseOkHttpClientFactory implements Factory<OkHttpClient> {
  @Override
  public OkHttpClient get() {
    return providesBaseOkHttpClient();
  }

  public static NetworkModule_Companion_ProvidesBaseOkHttpClientFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OkHttpClient providesBaseOkHttpClient() {
    return Preconditions.checkNotNullFromProvides(NetworkModule.Companion.providesBaseOkHttpClient());
  }

  private static final class InstanceHolder {
    static final NetworkModule_Companion_ProvidesBaseOkHttpClientFactory INSTANCE = new NetworkModule_Companion_ProvidesBaseOkHttpClientFactory();
  }
}

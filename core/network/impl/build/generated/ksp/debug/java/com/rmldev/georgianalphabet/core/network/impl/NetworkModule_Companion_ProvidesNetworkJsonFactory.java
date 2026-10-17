package com.rmldev.georgianalphabet.core.network.impl;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.serialization.json.Json;

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
public final class NetworkModule_Companion_ProvidesNetworkJsonFactory implements Factory<Json> {
  @Override
  public Json get() {
    return providesNetworkJson();
  }

  public static NetworkModule_Companion_ProvidesNetworkJsonFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static Json providesNetworkJson() {
    return Preconditions.checkNotNullFromProvides(NetworkModule.Companion.providesNetworkJson());
  }

  private static final class InstanceHolder {
    static final NetworkModule_Companion_ProvidesNetworkJsonFactory INSTANCE = new NetworkModule_Companion_ProvidesNetworkJsonFactory();
  }
}

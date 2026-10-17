package com.rmldev.georgianalphabet.core.network.impl;

import com.rmldev.georgianalphabet.core.network.api.AuthTokenRefresher;
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
public final class DjcAppAuthenticator_Factory implements Factory<DjcAppAuthenticator> {
  private final Provider<AuthTokenRefresher> tokenRefresherProvider;

  private DjcAppAuthenticator_Factory(Provider<AuthTokenRefresher> tokenRefresherProvider) {
    this.tokenRefresherProvider = tokenRefresherProvider;
  }

  @Override
  public DjcAppAuthenticator get() {
    return newInstance(tokenRefresherProvider);
  }

  public static DjcAppAuthenticator_Factory create(
      Provider<AuthTokenRefresher> tokenRefresherProvider) {
    return new DjcAppAuthenticator_Factory(tokenRefresherProvider);
  }

  public static DjcAppAuthenticator newInstance(
      javax.inject.Provider<AuthTokenRefresher> tokenRefresher) {
    return new DjcAppAuthenticator(tokenRefresher);
  }
}

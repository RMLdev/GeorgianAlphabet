package com.rmldev.georgianalphabet.core.network.impl;

import com.rmldev.georgianalphabet.core.network.api.AccessTokenProvider;
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
public final class AuthHeaderInterceptor_Factory implements Factory<AuthHeaderInterceptor> {
  private final Provider<AccessTokenProvider> accessTokenProvider;

  private AuthHeaderInterceptor_Factory(Provider<AccessTokenProvider> accessTokenProvider) {
    this.accessTokenProvider = accessTokenProvider;
  }

  @Override
  public AuthHeaderInterceptor get() {
    return newInstance(accessTokenProvider.get());
  }

  public static AuthHeaderInterceptor_Factory create(
      Provider<AccessTokenProvider> accessTokenProvider) {
    return new AuthHeaderInterceptor_Factory(accessTokenProvider);
  }

  public static AuthHeaderInterceptor newInstance(AccessTokenProvider accessTokenProvider) {
    return new AuthHeaderInterceptor(accessTokenProvider);
  }
}

package com.rmldev.georgianalphabet.core.datastore.di;

import android.content.Context;
import com.rmldev.georgianalphabet.core.datastore.AeadSerializerCreator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DataStoreModule_ProvidesAeadSerializerCreatorFactory implements Factory<AeadSerializerCreator> {
  private final Provider<Context> applicationContextProvider;

  private DataStoreModule_ProvidesAeadSerializerCreatorFactory(
      Provider<Context> applicationContextProvider) {
    this.applicationContextProvider = applicationContextProvider;
  }

  @Override
  public AeadSerializerCreator get() {
    return providesAeadSerializerCreator(applicationContextProvider.get());
  }

  public static DataStoreModule_ProvidesAeadSerializerCreatorFactory create(
      Provider<Context> applicationContextProvider) {
    return new DataStoreModule_ProvidesAeadSerializerCreatorFactory(applicationContextProvider);
  }

  public static AeadSerializerCreator providesAeadSerializerCreator(Context applicationContext) {
    return Preconditions.checkNotNullFromProvides(DataStoreModule.INSTANCE.providesAeadSerializerCreator(applicationContext));
  }
}

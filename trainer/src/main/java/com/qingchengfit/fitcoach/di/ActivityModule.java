package com.qingchengfit.fitcoach.di;

import com.qingchengfit.fitcoach.activity.FragActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import javax.inject.Singleton;

@Module
public abstract class ActivityModule {

  @Singleton
  @ContributesAndroidInjector()
  abstract FragActivity contributeFragActivityInjector();
}

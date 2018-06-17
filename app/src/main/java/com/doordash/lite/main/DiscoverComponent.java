package com.doordash.lite.main;

import com.doordash.lite.app.ApplicationComponent;
import com.doordash.lite.app.dagger.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = DiscoverModule.class)
interface DiscoverComponent {
    void inject(DiscoverActivity discoverActivity);
}


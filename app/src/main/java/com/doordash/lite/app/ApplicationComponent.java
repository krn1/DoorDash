package com.doordash.lite.app;

import com.doordash.repository.network.NetworkModule;
import com.doordash.repository.network.RestApi;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(DoorDashLiteApp application);

    DoorDashLiteApp application();

    RestApi restApi();

}

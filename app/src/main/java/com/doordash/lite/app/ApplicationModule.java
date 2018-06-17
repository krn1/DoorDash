package com.doordash.lite.app;

import dagger.Module;
import dagger.Provides;

@Module
class ApplicationModule {

    private final DoorDashLiteApp application;

    ApplicationModule(DoorDashLiteApp application) {
        this.application = application;
    }

    @Provides
    public DoorDashLiteApp provideApplication() {
        return application;
    }

}

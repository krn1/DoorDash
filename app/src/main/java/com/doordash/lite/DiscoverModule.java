package com.doordash.lite;

import com.doordash.lite.dagger.PerActivity;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
class DiscoverModule {
    private DiscoverContract.View view;
    DiscoverModule(DiscoverContract.View view) {
        this.view = view;
    }

    @PerActivity
    @Provides
    DiscoverContract.View providesView() {
        return view;
    }

    @PerActivity
    @Provides
    CompositeDisposable providesDisposable() {
        return new CompositeDisposable();
    }
}

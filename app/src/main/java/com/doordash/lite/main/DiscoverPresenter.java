package com.doordash.lite.main;

import com.doordash.lite.app.dagger.PerActivity;
import com.doordash.repository.model.Restaurant;
import com.doordash.repository.network.RestApi;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

@PerActivity
class DiscoverPresenter implements DiscoverContract.Presenter {

    private DiscoverContract.View view;
    private CompositeDisposable disposable;
    private RestApi apiService;

    @Inject
    DiscoverPresenter(DiscoverContract.View view,
                      RestApi apiService,
                      CompositeDisposable disposable) {
        this.view = view;
        this.apiService = apiService;
        this.disposable = disposable;
    }

    @Override
    public void start() {
        getRestaurants();
    }

    @Override
    public void stop() {

    }

    void getRestaurants() {
        disposable.add(apiService.getRestaurants("37.422740", "-122.139956", 0, 7)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Restaurant>>() {
                    @Override
                    public void onNext(List<Restaurant> restaurants) {
                        Timber.e("We got size %d\n  %s", restaurants.size(), restaurants.toString());
                        view.showRestaurants(restaurants);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.showError(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }
}

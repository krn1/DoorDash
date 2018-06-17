package com.doordash.lite.main;

import android.support.annotation.VisibleForTesting;

import com.doordash.lite.app.dagger.PerActivity;
import com.doordash.repository.model.Restaurant;
import com.doordash.repository.network.RestApi;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

@PerActivity
class DiscoverPresenter implements DiscoverContract.Presenter {

    private DiscoverContract.View view;
    private CompositeDisposable disposable;
    private RestApi apiService;

    private static int PAGE_LIMIT = 50;

    @VisibleForTesting
    int pageOffset = 0;

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
        loadFirstPage();
    }

    @Override
    public void stop() {
        disposable.clear();
    }

    @Override
    public void loadFirstPage() {
        pageOffset = 0;
        loadMorePages();
    }

    @Override
    public void loadMorePages() {
        view.showSpinner();
        disposable.add(apiService.getRestaurants("37.422740", "-122.139956", pageOffset, PAGE_LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Restaurant>>() {
                    @Override
                    public void onNext(List<Restaurant> restaurantList) {
                        //                        Timber.e("We got size %d\n  %s", restaurantList.size(), restaurantList.toString());
                        onRetrieveComplete(restaurantList);
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

    private void onRetrieveComplete(List<Restaurant> restaurantList) {
        view.hideSpinner();

        if (restaurantList.isEmpty()) {
            view.showError(null);
            return;
        }

        if (view.isRefreshing()) {
            view.refreshFeed(restaurantList);
        } else {
            view.showRestaurants(restaurantList);
        }

        // increment the pageOffset
        pageOffset++;
    }

}

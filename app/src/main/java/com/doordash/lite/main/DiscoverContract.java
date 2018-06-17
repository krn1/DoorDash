package com.doordash.lite.main;

import com.doordash.repository.model.Restaurant;

import java.util.List;

interface DiscoverContract {
    interface View {
        void showSpinner();

        void hideSpinner();

        void showRestaurants(List<Restaurant> restaurants);

        void refreshFeed(List<Restaurant> restaurants);

        boolean isRefreshing();

        void showError(String message);
    }

    interface Presenter {

        void start();

        void stop();

        void loadFirstPage();

        void loadMorePages();
    }
}

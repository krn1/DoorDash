package com.doordash.lite.main;

import com.doordash.repository.model.Restaurant;

import java.util.List;

interface DiscoverContract {
    interface View {
        void showRestaurants(List<Restaurant> restaurants);

        void showError(String message);
    }

    interface Presenter {

        void start();

        void stop();

    }
}

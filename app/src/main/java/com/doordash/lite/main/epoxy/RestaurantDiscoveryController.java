package com.doordash.lite.main.epoxy;

import com.airbnb.epoxy.EpoxyController;
import com.doordash.repository.model.Restaurant;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RestaurantDiscoveryController extends EpoxyController {

    private Set<Restaurant> restaurants;

    public RestaurantDiscoveryController() {
        restaurants = new LinkedHashSet<>();
    }

    @Override
    protected void buildModels() {

        for (Restaurant restaurant : restaurants) {
            new RestaurantListItemModel_()
                    .id(restaurant.getId())
                    .content(restaurant)
                    .favorite(true)
                    .addTo(this);
        }
    }

    public void addPage(List<Restaurant> restaurantList) {
        restaurants.addAll(restaurantList);
        requestModelBuild();
    }

    public void refreshFeed(List<Restaurant> restaurantList) {
        clear();
        addPage(restaurantList);
    }

    void clear() {
        restaurants.clear();
        requestModelBuild();
    }

}

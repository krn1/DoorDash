package com.doordash.lite.main;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyRecyclerView;
import com.doordash.lite.R;
import com.doordash.lite.app.DoorDashLiteApp;
import com.doordash.lite.main.epoxy.RestaurantDiscoveryController;
import com.doordash.repository.model.Restaurant;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DiscoverActivity extends AppCompatActivity implements DiscoverContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.list)
    EpoxyRecyclerView list;

    @BindView(R.id.empty_view)
    TextView emptyView;

    @Inject
    DiscoverPresenter presenter;

    private RestaurantDiscoveryController listController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        ButterKnife.bind(this);

        setupToolbar();
        setupEpoxy();
        getComponent().inject(this);

        presenter.getRestaurants();
    }

    //region override

    @Override
    public void showRestaurants(List<Restaurant> restaurants) {
        if (!restaurants.isEmpty()) {
            listController.setContents(restaurants);
            emptyView.setVisibility(View.GONE);
        } else {
            showError(getResources().getString(R.string.empty_list));
        }
    }

    @Override
    public void showError(String message) {
        emptyView.setVisibility(View.VISIBLE);
        Timber.e("Error " + message);
    }

    // endregion

    // region private
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setupEpoxy() {
        listController = new RestaurantDiscoveryController();
        list.setController(listController);
    }

    private DiscoverComponent getComponent() {
        return DaggerDiscoverComponent.builder()
                .applicationComponent(((DoorDashLiteApp) getApplication()).getComponent())
                .discoverModule(new DiscoverModule(this))
                .build();
    }
    // endregion private
}

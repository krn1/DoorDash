package com.doordash.lite.main;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyRecyclerView;
import com.doordash.lite.R;
import com.doordash.lite.app.DoorDashLiteApp;
import com.doordash.lite.main.epoxy.RestaurantDiscoveryController;
import com.doordash.repository.model.Restaurant;
import com.doordash.util.PaginationListenerUtil;

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

    @BindView(R.id.progress_bar_container)
    FrameLayout progressBarContainer;

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
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
    }
    //region override

    @Override
    public void showSpinner() {
        progressBarContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSpinner() {
        progressBarContainer.setVisibility(View.GONE);
    }

    @Override
    public void showRestaurants(List<Restaurant> restaurants) {
        listController.addPage(restaurants);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void refreshFeed(List<Restaurant> restaurants) {
        //TODO
    }

    @Override
    public boolean isRefreshing() {
        //TODO
        return false;
    }

    @Override
    public void showError(String message) {
        emptyView.setVisibility(View.VISIBLE);
        hideSpinner();
        if (message == null) {
            message = getResources().getString(R.string.empty_list);
        }
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


        LinearLayoutManager layoutManager = (LinearLayoutManager) list.getLayoutManager();
        list.addOnScrollListener(new PaginationListenerUtil(layoutManager, null) {
            @Override
            public void loadNextPage() {
                if (!isLoading()) {
                    presenter.loadRestaurants();
                }
            }
        });
    }

    private boolean isLoading() {
        return progressBarContainer.getVisibility() == View.VISIBLE;
    }

    private DiscoverComponent getComponent() {
        return DaggerDiscoverComponent.builder()
                .applicationComponent(((DoorDashLiteApp) getApplication()).getComponent())
                .discoverModule(new DiscoverModule(this))
                .build();
    }
    // endregion private
}

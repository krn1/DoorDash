package com.doordash.lite.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class DiscoverActivity extends AppCompatActivity implements DiscoverContract.View {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout pullToRefresh;

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
    private LinearLayoutManager layoutManager;

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
        fetchFirstPage();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
    }
    //region override

    @Override
    public void showSpinner() {
        if (!isRefreshing()) {
            progressBarContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideSpinner() {
        progressBarContainer.setVisibility(View.GONE);
    }

    @Override
    public void showRestaurants(List<Restaurant> restaurants) {
        listController.addPage(restaurants);
        pullToRefresh.setRefreshing(false);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void refreshFeed(List<Restaurant> restaurants) {
        pullToRefresh.setRefreshing(false);
        listController.refreshFeed(restaurants);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public boolean isRefreshing() {
        return pullToRefresh.isRefreshing();
    }

    @Override
    public void showError(String message) {
        emptyView.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);
        hideSpinner();
        if (message == null) {
            message = getResources().getString(R.string.error_msg);
        }

        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
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
        list.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        listController = new RestaurantDiscoveryController();
        list.setController(listController);

        layoutManager = (LinearLayoutManager) list.getLayoutManager();
        list.addOnScrollListener(new PaginationListenerUtil(layoutManager, pullToRefresh) {
            @Override
            public void loadNextPage() {
                if (!isLoading()) {
                    presenter.loadMorePages();
                }
            }
        });

        pullToRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        pullToRefresh.setOnRefreshListener(this::refreshFeed);
    }

    private boolean isLoading() {
        return progressBarContainer.getVisibility() == View.VISIBLE;
    }

    private void refreshFeed() {
        pullToRefresh.setRefreshing(true);
        pullToRefresh.postDelayed(this::fetchFirstPage, 1000);
    }

    private void fetchFirstPage() {
        presenter.start();
    }

    private DiscoverComponent getComponent() {
        return DaggerDiscoverComponent.builder()
                .applicationComponent(((DoorDashLiteApp) getApplication()).getComponent())
                .discoverModule(new DiscoverModule(this))
                .build();
    }
    // endregion private
}

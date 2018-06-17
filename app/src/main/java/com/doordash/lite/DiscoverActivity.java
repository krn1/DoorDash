package com.doordash.lite;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyRecyclerView;
import com.doordash.lite.app.DoorDashLiteApp;
import com.doordash.repository.model.Restaurant;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

import static android.view.View.GONE;

public class DiscoverActivity extends AppCompatActivity implements DiscoverContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.list)
    EpoxyRecyclerView list;

    @BindView(R.id.empty_view)
    TextView emptyView;

    private CompositeDisposable disposable;

    @Inject
    DiscoverPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        ButterKnife.bind(this);

        setupToolbar();
        getComponent().inject(this);
        disposable = new CompositeDisposable();

        presenter.getRestaurants();
    }

    // region private
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    private DiscoverComponent getComponent() {
        return DaggerDiscoverComponent.builder()
                .applicationComponent(((DoorDashLiteApp) getApplication()).getComponent())
                .discoverModule(new DiscoverModule(this))
                .build();
    }

    @Override
    public void showRestaurants(List<Restaurant> restaurants) {
        emptyView.setVisibility(GONE);
    }

    @Override
    public void showError(String message) {
        emptyView.setVisibility(View.VISIBLE);
    }
    // endregion private
}

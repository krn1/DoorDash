package com.doordash.lite.main;

import com.doordash.lite.utils.TrampolineSchedulerUtils;
import com.doordash.repository.model.Restaurant;
import com.doordash.repository.network.RestApi;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class DiscoverPresenterTest {
    @Mock
    private DiscoverContract.View view;
    @Mock
    private RestApi apiService;

    private CompositeDisposable disposable;

    private DiscoverPresenter presenter;

    @BeforeClass
    public static void setUpClass() {
        TrampolineSchedulerUtils.convertSchedulersToTrampoline();
    }

    @Before
    public void setUp() throws Exception {
        disposable = spy(new CompositeDisposable());
        ;
        presenter = new DiscoverPresenter(view, apiService, disposable);
    }

    @Test
    public void emptyList() throws Exception {
        // Given
        List<Restaurant> emptyList = Collections.emptyList();
        when(apiService.getRestaurants("37.422740", "-122.139956", 0, 50)).thenReturn(Flowable.just(emptyList));

        // When
        presenter.start();

        // Then
        verify(disposable, times(1)).add(any(Disposable.class));
        verify(view, times(0)).showRestaurants(emptyList);
        verify(view, times(1)).showError(null);
    }

    @Test
    public void firstPageOfRestaurants() throws Exception {
        // Given
        List<Restaurant> firstPage = createRestaurantList(6);
        when(apiService.getRestaurants("37.422740", "-122.139956", 0, 50)).thenReturn(Flowable.just(firstPage));

        // When
        presenter.start();

        // Then
        verify(view, times(1)).showSpinner();
        verify(view, times(1)).hideSpinner();
        verify(disposable, times(1)).add(any(Disposable.class));
        verify(view, times(1)).showRestaurants(firstPage);
        assertEquals(presenter.pageOffset, 1);
    }

    @Test
    public void loadMoreRestaurants() throws Exception {
        // Given
        List<Restaurant> list = createRestaurantList(6);
        presenter.pageOffset = 2;
        when(apiService.getRestaurants("37.422740", "-122.139956", presenter.pageOffset, 50)).thenReturn(Flowable.just(list));

        // When
        presenter.loadMorePages();

        // Then
        verify(disposable, times(1)).add(any(Disposable.class));
        verify(view, times(1)).showRestaurants(list);
        assertEquals(presenter.pageOffset, 3);
    }

    @Test
    public void pullToRefresh() throws Exception {
        // Given
        List<Restaurant> firstPage = createRestaurantList(6);
        when(apiService.getRestaurants("37.422740", "-122.139956", 0, 50)).thenReturn(Flowable.just(firstPage));
        when(view.isRefreshing()).thenReturn(true);
        // When
        presenter.start();

        // Then
        verify(view, times(1)).showSpinner();
        verify(view, times(1)).hideSpinner();
        verify(view, times(1)).refreshFeed(firstPage);
        assertEquals(presenter.pageOffset, 1);
    }
    // region private
    private List<Restaurant> createRestaurantList(int size) {
        List<Restaurant> restaurantList = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            restaurantList.add(createRestaurant(i));
        }
        return restaurantList;
    }

    private Restaurant createRestaurant(int id) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        return restaurant;
    }
    //endregion
}

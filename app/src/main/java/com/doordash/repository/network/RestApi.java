package com.doordash.repository.network;


import com.doordash.repository.model.Restaurant;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {
    @GET("?")
    Flowable<List<Restaurant>> getRestaurants(@Query("lat") String lat,
                                              @Query("lng") String lng,
                                              @Query("offset") int offset,
                                              @Query("limit") int limit);

}

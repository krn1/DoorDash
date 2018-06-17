package com.doordash.lite.main.epoxy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airbnb.epoxy.ModelProp;
import com.airbnb.epoxy.ModelView;
import com.doordash.lite.R;
import com.doordash.repository.model.Restaurant;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class RestaurantListItem extends FrameLayout {
    @BindView(R.id.cover_img)
    SimpleDraweeView coverImg;

    @BindView(R.id.restaurant_name)
    TextView name;

    @BindView(R.id.restaurant_kind)
    TextView kind;

    @BindView(R.id.distance)
    TextView distanceStatus;

    public RestaurantListItem(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.view_restaurant_list_item, this);
        ButterKnife.bind(this);
    }

    @ModelProp
    public void setContent(@NonNull Restaurant restaurant) {
        coverImg.setImageURI(restaurant.getCoverImgUrl());
        name.setText(restaurant.getName());
        kind.setText(restaurant.getDescription());
        distanceStatus.setText(restaurant.getStatus());
    }
}

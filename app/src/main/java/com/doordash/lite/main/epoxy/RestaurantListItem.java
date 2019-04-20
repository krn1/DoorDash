package com.doordash.lite.main.epoxy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.ModelProp;
import com.airbnb.epoxy.ModelView;
import com.doordash.lite.R;
import com.doordash.repository.model.Restaurant;
import com.facebook.drawee.view.SimpleDraweeView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

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

    @BindView(R.id.favorite)
    ImageView favorite;

    boolean favoriteStatus = true;
    public RestaurantListItem(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.view_restaurant_list_item, this);
        ButterKnife.bind(this);

        favorite.setOnClickListener(
                button -> setFavorite(!favoriteStatus));
    }

    @ModelProp
    public void setContent(@NonNull Restaurant restaurant) {
        coverImg.setImageURI(restaurant.getCoverImgUrl());
        name.setText(restaurant.getName());
        kind.setText(restaurant.getDescription());
        distanceStatus.setText(restaurant.getStatus());
    }

    @ModelProp
    public void setFavorite(@NonNull Boolean favorited) {
        Timber.e("status "+favorited);
        if(favorited){
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_enabled);
            favorite.setImageDrawable(drawable);
        } else{
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_disabled);
            favorite.setImageDrawable(drawable);
        }

       favoriteStatus = favorited;
    }

}


package com.doordash.repository.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MerchantPromotion {

    @SerializedName("minimum_subtotal_monetary_fields")
    @Expose
    private MinimumSubtotalMonetaryFields minimumSubtotalMonetaryFields;
    @SerializedName("delivery_fee")
    @Expose
    private Object deliveryFee;
    @SerializedName("delivery_fee_monetary_fields")
    @Expose
    private DeliveryFeeMonetaryFields deliveryFeeMonetaryFields;
    @SerializedName("minimum_subtotal")
    @Expose
    private Object minimumSubtotal;
    @SerializedName("new_store_customers_only")
    @Expose
    private boolean newStoreCustomersOnly;
    @SerializedName("id")
    @Expose
    private int id;

    public MinimumSubtotalMonetaryFields getMinimumSubtotalMonetaryFields() {
        return minimumSubtotalMonetaryFields;
    }

    public void setMinimumSubtotalMonetaryFields(MinimumSubtotalMonetaryFields minimumSubtotalMonetaryFields) {
        this.minimumSubtotalMonetaryFields = minimumSubtotalMonetaryFields;
    }

    public Object getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Object deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public DeliveryFeeMonetaryFields getDeliveryFeeMonetaryFields() {
        return deliveryFeeMonetaryFields;
    }

    public void setDeliveryFeeMonetaryFields(DeliveryFeeMonetaryFields deliveryFeeMonetaryFields) {
        this.deliveryFeeMonetaryFields = deliveryFeeMonetaryFields;
    }

    public Object getMinimumSubtotal() {
        return minimumSubtotal;
    }

    public void setMinimumSubtotal(Object minimumSubtotal) {
        this.minimumSubtotal = minimumSubtotal;
    }

    public boolean isNewStoreCustomersOnly() {
        return newStoreCustomersOnly;
    }

    public void setNewStoreCustomersOnly(boolean newStoreCustomersOnly) {
        this.newStoreCustomersOnly = newStoreCustomersOnly;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

}

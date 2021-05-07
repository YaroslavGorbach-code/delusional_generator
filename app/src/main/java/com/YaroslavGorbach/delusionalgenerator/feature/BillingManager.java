package com.YaroslavGorbach.delusionalgenerator.feature;
import android.app.Activity;
import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetailsParams;

import java.util.ArrayList;
import java.util.List;

public class BillingManager {

    public interface Callback{
        void onAdRemoved();
    }

    private BillingClient mBillingClient;

    public BillingManager(Activity activity, Callback callback) {
        initBillingClient(activity, callback);
    }

    private void initBillingClient(Activity activity, Callback callback){
        mBillingClient = BillingClient.newBuilder(activity)
                .setListener((billingResult, purchases) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases!=null){
                        for (Purchase purchase :purchases) {
                            handlePurchase(purchase, callback);
                        }
                    }
                }).enablePendingPurchases().build();
    }

    public void showPurchasesDialog(Activity activity){
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    List<String> skuList = new ArrayList<>();
                    skuList.add("remove_ad");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

                    mBillingClient.querySkuDetailsAsync(params.build(), (billingResult2, skuDetailsList) -> {
                        if (skuDetailsList != null){
                            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetailsList.get(0))
                                    .build();
                            int responseCode = mBillingClient.launchBillingFlow(activity, billingFlowParams).getResponseCode();
                        }
                    });
                }
            }

            @Override
            public void onBillingServiceDisconnected() { }
        });
    }
    public void queryPurchases(Callback callback){
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                List<Purchase> purchases = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList();
                if (purchases!=null && !purchases.isEmpty()){

                    callback.onAdRemoved();
//                    mBillingClient.consumeAsync(
//                            ConsumeParams.newBuilder().setPurchaseToken(purchases.get(0).getPurchaseToken()).build(),
//                            (billingResult1, s) -> { });
                }
            }
            @Override
            public void onBillingServiceDisconnected() { }
        });
    }

    private void handlePurchase(Purchase purchase, Callback callback) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> callback.onAdRemoved());
            }
        }
    }
}
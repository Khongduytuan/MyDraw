package com.eagletech.mydraw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.amazon.device.drm.LicensingService
import com.amazon.device.iap.PurchasingListener
import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.FulfillmentResult
import com.amazon.device.iap.model.ProductDataResponse
import com.amazon.device.iap.model.PurchaseResponse
import com.amazon.device.iap.model.PurchaseUpdatesResponse
import com.amazon.device.iap.model.UserDataResponse
import com.eagletech.mydraw.data.MyData
import com.eagletech.mydraw.databinding.ActivityBuyBinding

class BuyActivity : AppCompatActivity() {
    private lateinit var buyBinding: ActivityBuyBinding
    private lateinit var myData: MyData
    private lateinit var currentUserId: String
    private lateinit var currentMarketplace: String

    // Phải thêm sku các gói vào ứng dụng
    companion object {
        const val sub5Saves = "com.eagletech.mydraw.buy5save"
        const val sub10Saves = "com.eagletech.mydraw.buy10save"
        const val sub15Saves = "com.eagletech.mydraw.buy15save"
        const val subPre = "com.eagletech.mydraw.premium"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buyBinding = ActivityBuyBinding.inflate(layoutInflater)
        setContentView(buyBinding.root)
        myData = MyData.getInstance(this)
        setupIAPOnCreate()
        setClickItems()

    }

    private fun setClickItems() {
        buyBinding.buttonSave5.setOnClickListener {
//            myData.addSaves(3)
            PurchasingService.purchase(sub5Saves)
        }
        buyBinding.buttonSave10.setOnClickListener {
            PurchasingService.purchase(sub10Saves)
        }
        buyBinding.buttonSave15.setOnClickListener {
            PurchasingService.purchase(sub15Saves)
        }
        buyBinding.buttonSaveSub.setOnClickListener {
            PurchasingService.purchase(subPre)
//            dataSharedPreferences.isPremium = true
        }
        buyBinding.buttonExit.setOnClickListener { finish() }
    }

    private fun setupIAPOnCreate() {
        val purchasingListener: PurchasingListener = object : PurchasingListener {
            override fun onUserDataResponse(response: UserDataResponse) {
                when (response.requestStatus!!) {
                    UserDataResponse.RequestStatus.SUCCESSFUL -> {
                        currentUserId = response.userData.userId
                        currentMarketplace = response.userData.marketplace
                        myData.currentUserId(currentUserId)
                        Log.v("IAP SDK", "loaded userdataResponse")
                    }

                    UserDataResponse.RequestStatus.FAILED, UserDataResponse.RequestStatus.NOT_SUPPORTED ->
                        Log.v("IAP SDK", "loading failed")
                }
            }

            override fun onProductDataResponse(productDataResponse: ProductDataResponse) {
                when (productDataResponse.requestStatus) {
                    ProductDataResponse.RequestStatus.SUCCESSFUL -> {
                        val products = productDataResponse.productData
                        for (key in products.keys) {
                            val product = products[key]
                            Log.v(
                                "Product:", String.format(
                                    "Product: %s\n Type: %s\n SKU: %s\n Price: %s\n Description: %s\n",
                                    product!!.title,
                                    product.productType,
                                    product.sku,
                                    product.price,
                                    product.description
                                )
                            )
                        }
                        //get all unavailable SKUs
                        for (s in productDataResponse.unavailableSkus) {
                            Log.v("Unavailable SKU:$s", "Unavailable SKU:$s")
                        }
                    }

                    ProductDataResponse.RequestStatus.FAILED -> Log.v("FAILED", "FAILED")
                    else -> {}
                }
            }

            override fun onPurchaseResponse(purchaseResponse: PurchaseResponse) {
                when (purchaseResponse.requestStatus) {
                    PurchaseResponse.RequestStatus.SUCCESSFUL -> {

                        if (purchaseResponse.receipt.sku == sub5Saves) {
                            myData.addSaves(5)
                        }
                        if (purchaseResponse.receipt.sku == sub10Saves) {
                            myData.addSaves(10)
                        }
                        if (purchaseResponse.receipt.sku == sub15Saves) {
                            myData.addSaves(15)
                        }

                        PurchasingService.notifyFulfillment(
                            purchaseResponse.receipt.receiptId,
                            FulfillmentResult.FULFILLED
                        )

                        myData.isPremiumSaves = !purchaseResponse.receipt.isCanceled
                        Log.v("FAILED", "FAILED")
                    }

                    PurchaseResponse.RequestStatus.FAILED -> {}
                    else -> {}
                }
            }

            override fun onPurchaseUpdatesResponse(response: PurchaseUpdatesResponse) {
                // Process receipts
                when (response.requestStatus) {
                    PurchaseUpdatesResponse.RequestStatus.SUCCESSFUL -> {
                        for (receipt in response.receipts) {
                            myData.isPremiumSaves = !receipt.isCanceled
                        }
                        if (response.hasMore()) {
                            PurchasingService.getPurchaseUpdates(false)
                        }

                    }

                    PurchaseUpdatesResponse.RequestStatus.FAILED -> Log.d("FAILED", "FAILED")
                    else -> {}
                }
            }
        }
        PurchasingService.registerListener(this, purchasingListener)
        Log.d(
            "DetailBuyAct",
            "Appstore SDK Mode: " + LicensingService.getAppstoreSDKMode()
        )
    }


    override fun onResume() {
        super.onResume()
        PurchasingService.getUserData()
        val productSkus: MutableSet<String> = HashSet()
        productSkus.add(subPre)
        productSkus.add(sub5Saves)
        productSkus.add(sub10Saves)
        productSkus.add(sub15Saves)
        PurchasingService.getProductData(productSkus)
        PurchasingService.getPurchaseUpdates(false)
    }
}
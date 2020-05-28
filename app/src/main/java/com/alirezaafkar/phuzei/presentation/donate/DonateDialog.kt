package com.alirezaafkar.phuzei.presentation.donate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alirezaafkar.phuzei.R
import com.alirezaafkar.phuzei.util.toast
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.*
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_donate.*

/**
 * Created by Alireza Afkar on 5/28/20.
 */
class DonateDialog : BottomSheetDialogFragment(), PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_donate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBillingClient()
    }

    private fun setupBillingClient() {
        billingClient = newBuilder(requireContext())
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingResponseCode.OK) {
                    querySkuDetails()
                } else {
                    dismiss()
                }
            }

            override fun onBillingServiceDisconnected() {
                dismiss()
            }
        })
    }

    private fun querySkuDetails() {
        val skuList = mutableListOf<String>().apply {
            add(DONATE_1)
            add(DONATE_5)
            add(DONATE_10)
        }
        val params = SkuDetailsParams.newBuilder().apply {
            setSkusList(skuList).setType(SkuType.INAPP)
        }
        billingClient.querySkuDetailsAsync(
            params.build()
        ) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingResponseCode.OK) {
                recyclerView.adapter = DonateAdapter(skuDetailsList) {
                    launchBillingFlow(it)
                }
            }
        }
    }

    private fun launchBillingFlow(skuDetails: SkuDetails) {
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        billingClient.launchBillingFlow(activity, flowParams)
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult?,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult?.responseCode == BillingResponseCode.OK && purchases?.size ?: 0 > 0) {
            context?.toast(R.string.purchase_ok)
        }
        dismiss()
    }

    companion object {
        private const val DONATE_1 = "donate1"
        private const val DONATE_5 = "donate5"
        private const val DONATE_10 = "donate10"

        fun newInstance(): DonateDialog {
            return DonateDialog()
        }
    }
}

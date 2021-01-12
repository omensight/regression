package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.DialogFragmentOfferPremiumFeatureBinding
import com.alphemsoft.education.regression.ui.base.BaseDialogFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.launch

class PremiumFeatureDialogFragment : BaseDialogFragment<DialogFragmentOfferPremiumFeatureBinding>(
    R.layout.dialog_fragment_offer_premium_feature
) {
    private lateinit var rewardedAd: RewardedAd
    private lateinit var onPremiumDecisionListener: OnPremiumDecisionListener
    private var featureId: Int = -1
    private var featureName: Int = -1
    fun show(
        manager: FragmentManager,
        onPremiumDecisionListener: OnPremiumDecisionListener,
        featureId: Int,
        @StringRes featureName: Int,
    ) {
        this.onPremiumDecisionListener = onPremiumDecisionListener
        this.featureId = featureId
        this.featureName = featureName
        super.show(manager, "premium_decision_dialog_fragment")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUi()
        setupNativeRewardedVideo()
    }

    private lateinit var rewardedAdLoadCallback: RewardedAdLoadCallback
    private lateinit var rewardedAdCallback: RewardedAdCallback

    private fun setupNativeRewardedVideo() {
        rewardedAd = RewardedAd(requireContext().applicationContext,
            "ca-app-pub-3940256099942544/5224354917")
        rewardedAdCallback = object : RewardedAdCallback() {
            override fun onUserEarnedReward(rewardItem: RewardItem) {
                super.onRewardedAdClosed()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.user_earned_reward),
                    Toast.LENGTH_LONG
                ).show()
                onPremiumDecisionListener.onRewardedVideoWatched(featureId)

            }

            override fun onRewardedAdClosed() {
                super.onRewardedAdClosed()
                dismiss()
            }
        }

        rewardedAdLoadCallback = object : RewardedAdLoadCallback() {

            override fun onRewardedAdLoaded() {
                dataBinding.btWatchVideo.isEnabled = true
                super.onRewardedAdLoaded()
            }

            override fun onRewardedAdFailedToLoad(p0: LoadAdError?) {
                super.onRewardedAdFailedToLoad(p0)
            }
        }

        rewardedAd.loadAd(AdRequest.Builder().build(), rewardedAdLoadCallback)

    }

    private fun setupUi() {
        dataBinding.viewBackground
        dataBinding.tvTitle.text = getString(featureName)
        dataBinding.btCancel.setOnClickListener {
            dismiss()
        }
        dataBinding.btSubscribe.setOnClickListener {
            dismiss()
            coroutineHandler.foregroundScope.launch {
                onPremiumDecisionListener.onGetASubscriptionSelected(featureId)
            }
        }
        dataBinding.btWatchVideo.setOnClickListener {
            if (rewardedAd.isLoaded){
                rewardedAd.show(requireActivity(), rewardedAdCallback)
            }
        }
    }

    interface OnPremiumDecisionListener {
        suspend fun onGetASubscriptionSelected(featureId: Int)
        fun onRewardedVideoWatched(featureId: Int)
    }
}
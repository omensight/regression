package com.alphemsoft.education.regression.premium

enum class PremiumSubscriptionState(val state: Int) {
    NOT_SUBSCRIBED(0),
    TEMPORARY_ACCESS(1),
    SUBSCRIBED(2),
    CANCELLED(3)
}
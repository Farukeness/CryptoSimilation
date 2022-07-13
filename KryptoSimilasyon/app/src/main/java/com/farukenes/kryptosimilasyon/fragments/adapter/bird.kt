package com.farukenes.kryptosimilasyon.fragments.adapter

import com.google.gson.annotations.SerializedName

data class bird (

    @SerializedName("volume") val volume : Double,
    @SerializedName("price_change") val price_change : Double,
    @SerializedName("price_change_pct") val price_change_pct : Double,
    @SerializedName("volume_change") val volume_change : Double,
    @SerializedName("volume_change_pct") val volume_change_pct : Double,
    @SerializedName("market_cap_change") val market_cap_change : Double,
    @SerializedName("market_cap_change_pct") val market_cap_change_pct : Double
)
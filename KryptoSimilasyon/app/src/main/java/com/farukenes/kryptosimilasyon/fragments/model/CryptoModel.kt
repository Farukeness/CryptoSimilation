package com.farukenes.kryptosimilasyon.fragments.model

import com.farukenes.kryptosimilasyon.fragments.adapter.bird
import com.google.gson.annotations.SerializedName

data class CryptoModel(val currency: String,
                       val price:String,
                       val name:String,
                       val logo_url:String,
                       @SerializedName("1d") val priceChange : bird,
                       val rank:String,
                       val max_supply:String,
                       val market_cap:String,
                       val circulating_supply:String
) {
}
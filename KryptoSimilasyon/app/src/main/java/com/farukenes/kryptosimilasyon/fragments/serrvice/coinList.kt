package com.farukenes.kryptosimilasyon.fragments.serrvice

import com.farukenes.kryptosimilasyon.fragments.model.svtToPng

class coinList {
    fun coinListem():ArrayList<svtToPng> {
        val coinCurr=ArrayList<svtToPng>()
        val btc=svtToPng("BfdTC","Bitcoin")
        coinCurr.add(btc)
        return coinCurr
    }

}
package com.farukenes.kryptosimilasyon.fragments.serrvice

import com.farukenes.kryptosimilasyon.fragments.model.svtToPng
import java.net.URL

class svgList {

    fun svglist():ArrayList<svtToPng>{
        val urlPng=ArrayList<svtToPng>()
        val btc=svtToPng("BTC","https://cdn.pixabay.com/photo/2015/08/27/11/20/bitcoin-910307_960_720.png")
        urlPng.add(btc)
        val avax=svtToPng("AVAX","https://cryptoast.fr/wp-content/uploads/2020/09/avalanche-avax-logo.png")
        urlPng.add(avax)
        val eth=svtToPng("ETH","https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Ethereum_logo_2014.svg/256px-Ethereum_logo_2014.svg.png")
        urlPng.add(eth)
        val bnb=svtToPng("BNB","https://cryptologos.cc/logos/binance-coin-bnb-logo.png?v=014")
        urlPng.add(bnb)
        val xrp=svtToPng("XRP","https://cryptologos.cc/logos/xrp-xrp-logo.png?v=014")
        urlPng.add(xrp)
        val ada = svtToPng("ADA","https://cdn4.iconfinder.com/data/icons/crypto-currency-and-coin-2/256/cardano_ada-512.png")
        urlPng.add(ada)
        val celo=svtToPng("CELO","https://cryptologos.cc/logos/celo-celo-logo.png?v=014")
        urlPng.add(celo)
        val dent=svtToPng("DENT","https://research.binance.com/static/images/projects/dent/logo.png")
        urlPng.add(dent)
        val jasmy= svtToPng("JASMY","https://s2.coinmarketcap.com/static/img/coins/64x64/8425.png")
        urlPng.add(jasmy)
        val alice= svtToPng("ALICE","https://cdn.coingape.com/wp-content/uploads/2021/11/23212935/ae38a130-f032-11eb-b60d-8f6b9e8594f7_original.png")
        urlPng.add(alice)
        val eqx=svtToPng("EQX","https://dynamic-assets.coinbase.com/45e47235835f1b3c54d000bfc48ba160bd5a6ed481f7f0a5e90c60344c06c9a513d509310115d60480084008ff17ec7ec7e7c45da74dae034662070fcce7e60b/asset_icons/49f8d3779b8b46887790d392e3db2a07e20c2da12bb4aa6c7035bf51bd0e3b92.png")
        urlPng.add(eqx)
        val enj=svtToPng("ENJ","https://cryptologos.cc/logos/enjin-coin-enj-logo.png?v=014")
        urlPng.add(enj)
        val xtz= svtToPng("XTZ","https://seeklogo.com/images/T/tezos-xtz-logo-C96D3F7FB9-seeklogo.com.png")
        urlPng.add(xtz)
        val hua=svtToPng("HUAHUA","https://pbs.twimg.com/profile_images/1475847662666665988/1vXGldb8_400x400.jpg")
        urlPng.add(hua)
        return urlPng
    }
}
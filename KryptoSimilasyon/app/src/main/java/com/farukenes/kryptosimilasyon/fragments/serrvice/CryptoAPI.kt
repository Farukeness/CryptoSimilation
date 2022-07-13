package com.farukenes.kryptosimilasyon.fragments.serrvice

import com.farukenes.kryptosimilasyon.fragments.model.CryptoModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface CryptoAPI {
    //https://api.nomics.com/v1/prices?key=0b944e474bae5fff79cbbd8934b43e385df3a4d6
    @GET("currencies/ticker?key=ddb57f9e4d7d2d96a2fbe70ec8769d3d4b0773ce&ids=ETH,XTZ,XRP,ONT,RARE,RACA,ATA,1INCH,AAVE,BTC,AVAX,AGLD,AKRO,ALICE,ADA,ANKR,ARPA,ATOM,OCEAN,BADGER,BAKE,BNB,CAKE,CELO,CELR,CHZ,COTI,CTSI,DENT,REEF,PRQ,EQX,JASMY,COS,CRV,DYDX,EGLD,ENJ,FTM,GALA,HOT,HNT,NEAR,SCRT,KSM,ROSE,SXP,CSPR,BOBA,DFI,RMRK,JUNO,OCT,UMEE,FLAG,GAMI&interval=1d,&convert=USD&per-page=100&page=1")
    suspend fun getData(): Response<List<CryptoModel>>
}
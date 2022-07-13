package com.farukenes.kryptosimilasyon.fragments.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farukenes.kryptosimilasyon.R
import com.farukenes.kryptosimilasyon.fragments.model.CryptoModel
import com.squareup.picasso.Picasso
import com.bumptech.glide.RequestBuilder
import com.caverock.androidsvg.SVG
import com.farukenes.kryptosimilasyon.fragments.serrvice.svgList
import kotlinx.android.synthetic.main.row_layout_cuzdan.view.*





class cuzdanAdapter(private val CuzdanListesi:ArrayList<cuzdanListesi>,private val cryptoList : ArrayList<CryptoModel>):RecyclerView.Adapter<cuzdanAdapter.RowHolder>() {
    class RowHolder(view: View): RecyclerView.ViewHolder(view){
    }
    private var toplam:Int?=null
    private var ytoplam:Int?=0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout_cuzdan,parent,false)
        return RowHolder(view )
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        //CuzdanListesi.get(position).coinIsim
        //CuzdanListesi.get(position).coinAdet.toString()
        holder.itemView.coinName.text=CuzdanListesi.get(position).coinIsim
        holder.itemView.bakiyeView.text=CuzdanListesi.get(position).coinAdet.toString()
        if(CuzdanListesi.get(position).coinIsim=="USDT"){
            holder.itemView.coinName2.text="Tether"
            holder.itemView.currentPrice.text="1.0"
            holder.itemView.cost.text="1.0"
            holder.itemView.coinPrice.text=CuzdanListesi.get(position).coinAdet.toInt().toString()+" $"
            Glide.with(holder.itemView).load(R.drawable.tether).into(holder.itemView.coinImage)
        }
        for(i in 0..cryptoList.size-1){
            if(CuzdanListesi.get(position).coinIsim ==cryptoList.get(i).currency){
                holder.itemView.coinName2.text=cryptoList.get(i).name
                holder.itemView.cost.text=CuzdanListesi.get(position).costPrice.toString()
                holder.itemView.currentPrice.text=cryptoList.get(i).price
                //Picasso.get().load(cryptoList.get(i).logo_url).into(holder.itemView.coinImage)
                Glide.with(holder.itemView).load(cryptoList.get(i).logo_url).into(holder.itemView.coinImage)
                toplam = (cryptoList.get(i).price.toString().toFloat()*CuzdanListesi
                    .get(position)
                    .coinAdet.toString().toFloat()).toInt()
                holder.itemView.coinPrice.text= toplam.toString()+" $"
                ytoplam= toplam.toString().toIntOrNull()?.plus(ytoplam.toString().toIntOrNull()!!)
                for (a in 0..svgList().svglist().size-1){
                    if(CuzdanListesi.get(position).coinIsim== svgList().svglist().get(a).coinCurrency){
                        Glide.with(holder.itemView).load(svgList().svglist().get(a).coinUrl).into(holder.itemView.coinImage)
                    }
                }
            }
        }
        //println(ytoplam)


        //println(position)

        //println(cryptoList.get(position).currency)


    }

    override fun getItemCount(): Int {
        return  CuzdanListesi.count()

    }

}

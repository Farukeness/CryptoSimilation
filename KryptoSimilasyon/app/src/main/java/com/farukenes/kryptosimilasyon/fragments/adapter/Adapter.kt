package com.farukenes.kryptosimilasyon.fragments.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farukenes.kryptosimilasyon.R
import com.farukenes.kryptosimilasyon.fragments.model.CryptoModel
import com.farukenes.kryptosimilasyon.fragments.model.favList
import com.farukenes.kryptosimilasyon.fragments.serrvice.svgList
import com.farukenes.kryptosimilasyon.fragments.view.PiyasaFragments
import com.farukenes.kryptosimilasyon.fragments.view.PiyasaFragmentsDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.row_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class Adapter(private val cryptoList : ArrayList<CryptoModel>,private val listener:Listener,private val favListesi:ArrayList<favList>) : RecyclerView.Adapter<Adapter.RowHolder>(),Filterable {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var favKontrolcusu:Int?=0
    var coinFilterList = ArrayList<CryptoModel>()

    init {
        coinFilterList = cryptoList
    }
    private var recycylerViewAdapterFavList:favAdapter?=null
    interface Listener{
        fun onItemClick(cryptoModel: CryptoModel)
    }
    class RowHolder(view:View): RecyclerView.ViewHolder(view){
        fun bind(cryptoModel: CryptoModel,listener: Listener){
            itemView.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            //Glide.with(itemView).load("https://thumbnail.imgbin.com/3/13/12/imgbin-fenerbah-e-s-k-dream-league-soccer-adanaspor-logo-football-football-EYNfqss4fyukp8inxQTY6g2gN_t.jpg").into(itemView.doluList)
            Glide.with(itemView).load(R.drawable.ic_star_beyaz).into(itemView.doluList)

            for (a in 0..svgList().svglist().size-1){
                if(cryptoModel.currency== svgList().svglist().get(a).coinCurrency){
                    Glide.with(itemView).load(svgList().svglist().get(a).coinUrl).into(itemView.imageView2)
                    //Glide.with(itemView).load("https://www.pinclipart.com/picdir/big/394-3949390_computer-icons-clip-art-star-png-icon-transparent.png").into(itemView.doluList)

                }
            }
            /*
            itemView.textName.text=cryptoModel.currency
            itemView.textPrice.text=cryptoModel.price.substring(0,7)
            itemView.coinNameText.text=cryptoModel.name
            Glide.with(itemView).load(cryptoModel.logo_url).into(itemView.imageView2)
            Glide.with(itemView).load("https://www.pinclipart.com/picdir/big/394-3949390_computer-icons-clip-art-star-png-icon-transparent.png").into(itemView.doluu)
            for (a in 0..svgList().svglist().size-1){
                if(cryptoModel.currency== svgList().svglist().get(a).coinCurrency){
                    Glide.with(itemView).load(svgList().svglist().get(a).coinUrl).into(itemView.imageView2)
                    Glide.with(itemView).load("https://www.pinclipart.com/picdir/big/394-3949390_computer-icons-clip-art-star-png-icon-transparent.png").into(itemView.doluu)
                }
            }


            //itemView.changeText.text=cryptoModel.priceChange.price_change.toString()
            var priceChanged=cryptoModel.priceChange.price_change
            if(Math.signum(priceChanged).toString()=="1.0"){
                val ilkd = cryptoModel.price.toString().toFloat()-priceChanged.toString().toFloat()
                val sond=cryptoModel.price
                val degisim= sond.toString().toFloat()-ilkd.toString().toFloat()
                itemView.changeText.setBackgroundResource(R.drawable.yuzdelik)
                val son = ((degisim.toString().toFloat()/ilkd.toString().toFloat())*100)
                itemView.changeText.text="+% "+son.toString().substring(0,4)
                itemView.textPrice.setTextColor(Color.parseColor("#FF00BC05"))
            }else if (Math.signum(priceChanged).toString()=="-1.0"){
                val ilkd = cryptoModel.price.toString().toFloat()+priceChanged.toString().toFloat()
                val sond=cryptoModel.price
                val degisim= sond.toString().toFloat()-ilkd.toString().toFloat()
                itemView.changeText.setBackgroundResource(R.drawable.yuzdelik_kirmizi)
                val sonn = ((degisim.toString().toFloat()/ilkd.toString().toFloat())*100)
                itemView.changeText.text="-% "+ sonn.toString().substring(0,4)
                itemView.textPrice.setTextColor(Color.parseColor("#FFCF0000"))
            }*/
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
        auth = Firebase.auth
        db = Firebase.firestore

        return RowHolder(view)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        try{
            val coinFilterr = holder as RowHolder
            holder.bind(cryptoList[position],listener)
            coinFilterr.itemView.textName.text= coinFilterList[position].currency
            //coinFilterr.itemView.textName.text= coinList().coinListem()[position].coinCurrency
            coinFilterr.itemView.coinNameText.text=coinFilterList[position].name
            coinFilterr.itemView.textPrice.text=coinFilterList[position].price.substring(0,7)
            Glide.with(holder.itemView).load(coinFilterList[position].logo_url).into(holder.itemView.imageView2)

            for (a in 0..svgList().svglist().size-1){
                if(coinFilterList[position].currency== svgList().svglist().get(a).coinCurrency){
                    Glide.with(holder.itemView).load(svgList().svglist().get(a).coinUrl).into(holder.itemView.imageView2)
                    //Glide.with(holder.itemView).load("https://www.pinclipart.com/picdir/big/394-3949390_computer-icons-clip-art-star-png-icon-transparent.png").into(holder.itemView.doluu)
                }
            }

            for (i in 0..favListesi.size-1){
                if(favListesi.get(i).coinName==coinFilterList.get(position).currency){
                    holder.itemView.doluList.visibility=View.VISIBLE
                    holder.itemView.bossList.visibility=View.INVISIBLE
                    favKontrolcusu=1
                    Glide.with(holder.itemView).load(R.drawable.ic_dolu2).into(holder.itemView.doluList)
                }

            }

            var priceChanged=coinFilterList[position].priceChange.price_change
            if(Math.signum(priceChanged).toString()=="1.0"){
                val ilkd = coinFilterList[position].price.toString().toFloat()-priceChanged.toString().toFloat()
                val sond=coinFilterList[position].price
                val degisim= sond.toString().toFloat()-ilkd.toString().toFloat()
                holder.itemView.changeText.setBackgroundResource(R.drawable.yuzdelik)
                val son = ((degisim.toString().toFloat()/ilkd.toString().toFloat())*100)
                holder.itemView.changeText.text="+% "+son.toString().substring(0,4)
                holder.itemView.textPrice.setTextColor(Color.parseColor("#FF00BC05"))
            }else if (Math.signum(priceChanged).toString()=="-1.0"){
                val ilkd = coinFilterList[position].price.toString().toFloat()+priceChanged.toString().toFloat()
                val sond=coinFilterList[position].price
                val degisim= sond.toString().toFloat()-ilkd.toString().toFloat()
                holder.itemView.changeText.setBackgroundResource(R.drawable.yuzdelik_kirmizi)
                val sonn = ((degisim.toString().toFloat()/ilkd.toString().toFloat())*100)
                holder.itemView.changeText.text="-% "+ sonn.toString().substring(0,4)
                holder.itemView.textPrice.setTextColor(Color.parseColor("#FFCF0000"))
            }


            holder.itemView.setOnClickListener {
                val action = PiyasaFragmentsDirections.actionPiyasaFragmentsToTradeFragments(coinFilterList[position].currency,coinFilterList[position].price.toFloat(),favKontrolcusu!!.toInt())
                Navigation.findNavController(it).navigate(action)
            }

            holder.itemView.bossList.setOnClickListener{
                var coinName= cryptoList.get(position).currency
                val email = auth.currentUser?.email.toString()
                val rank=cryptoList.get(position).rank.toString().toInt()
                val coinListem = db.collection("favList").document(email)
                    .collection("ikinciListem")
                val data1 = hashMapOf(
                    "coinName" to coinName,
                    "coinRank" to rank
                )
                coinListem.document(coinName).set(data1)


            }

            holder.itemView.doluList.setOnClickListener {
                holder.itemView.doluList.visibility=View.INVISIBLE
                holder.itemView.bossList.visibility=View.VISIBLE
                val email = auth.currentUser?.email.toString()
                var coinName= cryptoList.get(position).currency
                db.collection("favList").document(email)
                    .collection("ikinciListem")
                    .document(coinName)
                    .delete()


            }



        }catch (e:Exception){

        }


    }

    override fun getItemCount(): Int {
        return coinFilterList.size

    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch= p0.toString()
                if(charSearch.isEmpty()){
                    coinFilterList=cryptoList
                }else{
                    val resultList=ArrayList<CryptoModel>()
                    for(row in cryptoList){
                        if(row.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))||row.currency.lowercase(
                                Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))){
                            resultList.add(row)
                        }
                    }
                    coinFilterList=resultList
                }
                val filterResults=FilterResults()
                filterResults.values=coinFilterList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                coinFilterList=p1?.values as ArrayList<CryptoModel>
                notifyDataSetChanged()
            }

        }
    }
}
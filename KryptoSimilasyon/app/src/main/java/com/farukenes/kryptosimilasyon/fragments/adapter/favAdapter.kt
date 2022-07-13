package com.farukenes.kryptosimilasyon.fragments.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farukenes.kryptosimilasyon.R
import com.farukenes.kryptosimilasyon.fragments.model.CryptoModel
import com.farukenes.kryptosimilasyon.fragments.model.favList
import com.farukenes.kryptosimilasyon.fragments.serrvice.svgList
import com.farukenes.kryptosimilasyon.fragments.view.PiyasaFragmentsDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.row_layout_favlist.view.bossList
import kotlinx.android.synthetic.main.row_layout_favlist.view.changeText
import kotlinx.android.synthetic.main.row_layout_favlist.view.coinNameText
import kotlinx.android.synthetic.main.row_layout_favlist.view.doluList
import kotlinx.android.synthetic.main.row_layout_favlist.view.imageView2
import kotlinx.android.synthetic.main.row_layout_favlist.view.textName
import kotlinx.android.synthetic.main.row_layout_favlist.view.textPrice
import java.util.*
import kotlin.collections.ArrayList

class favAdapter(private val cryptoList : ArrayList<CryptoModel>, private val favListesi:ArrayList<favList>) : RecyclerView.Adapter<favAdapter.RowHolder>(),Filterable {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var kontrol:Boolean?=false
    var coinFilterListe = ArrayList<favList>()
    var coinFilterListem = ArrayList<favList>()

    init {
        coinFilterListe = favListesi
    }


    class RowHolder(view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout_favlist,parent,false)
        auth = Firebase.auth
        db = Firebase.firestore
        return RowHolder(view)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        try {
            if(auth.currentUser!=null){
                holder.itemView.doluList.visibility=View.VISIBLE
                holder.itemView.bossList.visibility=View.INVISIBLE
                /*for (a in 0..cryptoList.size-1){
                    if(favListesi.get(position).coinName==cryptoList.get(a).currency){
                        holder.itemView.textName.text=cryptoList[a].currency
                        holder.itemView.textPrice.text=cryptoList.get(a).price.substring(0,7)
                        holder.itemView.coinNameText.text=cryptoList.get(a).name
                        Glide.with(holder.itemView).load(cryptoList.get(a).logo_url).into(holder.itemView.imageView2)
                        var priceChanged= cryptoList.get(a).priceChange.price_change
                        if(Math.signum(priceChanged).toString()=="1.0"){
                            val ilkd = cryptoList.get(a).price.toString().toFloat()-priceChanged.toString().toFloat()
                            val sond=cryptoList.get(a).price
                            val degisim= sond.toString().toFloat()-ilkd.toString().toFloat()
                            holder.itemView.changeText.setBackgroundResource(R.drawable.yuzdelik)
                            val son = ((degisim.toString().toFloat()/ilkd.toString().toFloat())*100)
                            holder.itemView.changeText.text="+% "+son.toString().substring(0,4)
                            holder.itemView.textPrice.setTextColor(Color.parseColor("#FF00BC05"))
                        }
                        else if (Math.signum(priceChanged).toString()=="-1.0"){
                            val ilkd = cryptoList.get(a).price.toString().toFloat()+priceChanged.toString().toFloat()
                            val sond=cryptoList.get(a).price
                            val degisim= sond.toString().toFloat()-ilkd.toString().toFloat()
                            holder.itemView.changeText.setBackgroundResource(R.drawable.yuzdelik_kirmizi)
                            val sonn = ((degisim.toString().toFloat()/ilkd.toString().toFloat())*100)
                            holder.itemView.changeText.text="-% "+ sonn.toString().substring(0,4)
                            holder.itemView.textPrice.setTextColor(Color.parseColor("#FFCF0000"))
                        }
                        holder.itemView.setOnClickListener {
                            val action= PiyasaFragmentsDirections.actionPiyasaFragmentsToTradeFragments(cryptoList.get(a).currency,cryptoList.get(a).price.toFloat())
                            Navigation.findNavController(it).navigate(action)
                        }
                        for (a in 0..svgList().svglist().size-1){
                            if(favListesi.get(position).coinName== svgList().svglist().get(a).coinCurrency){
                                Glide.with(holder.itemView).load(svgList().svglist().get(a).coinUrl).into(holder.itemView.imageView2)
                            }
                        }
                    }
                }*/
                for(a in 0..cryptoList.size-1){
                    if(cryptoList[a].currency==coinFilterListe[position].coinName){
                        holder.itemView.textName.text=cryptoList[a].currency
                        holder.itemView.textPrice.text=cryptoList.get(a).price.substring(0,7)
                        holder.itemView.coinNameText.text=cryptoList.get(a).name
                        Glide.with(holder.itemView).load(cryptoList.get(a).logo_url).into(holder.itemView.imageView2)
                        var priceChanged= cryptoList.get(a).priceChange.price_change
                        if(Math.signum(priceChanged).toString()=="1.0"){
                            val ilkd = cryptoList.get(a).price.toString().toFloat()-priceChanged.toString().toFloat()
                            val sond=cryptoList.get(a).price
                            val degisim= sond.toString().toFloat()-ilkd.toString().toFloat()
                            holder.itemView.changeText.setBackgroundResource(R.drawable.yuzdelik)
                            val son = ((degisim.toString().toFloat()/ilkd.toString().toFloat())*100)
                            holder.itemView.changeText.text="+% "+son.toString().substring(0,4)
                            holder.itemView.textPrice.setTextColor(Color.parseColor("#FF00BC05"))
                        }
                        else if (Math.signum(priceChanged).toString()=="-1.0"){
                            val ilkd = cryptoList.get(a).price.toString().toFloat()+priceChanged.toString().toFloat()
                            val sond=cryptoList.get(a).price
                            val degisim= sond.toString().toFloat()-ilkd.toString().toFloat()
                            holder.itemView.changeText.setBackgroundResource(R.drawable.yuzdelik_kirmizi)
                            val sonn = ((degisim.toString().toFloat()/ilkd.toString().toFloat())*100)
                            holder.itemView.changeText.text="-% "+ sonn.toString().substring(0,4)
                            holder.itemView.textPrice.setTextColor(Color.parseColor("#FFCF0000"))
                        }
                        holder.itemView.setOnClickListener {
                            val action= PiyasaFragmentsDirections.actionPiyasaFragmentsToTradeFragments(cryptoList.get(a).currency,cryptoList.get(a).price.toFloat())
                            Navigation.findNavController(it).navigate(action)
                        }
                        for (a in 0..svgList().svglist().size-1){
                            if(coinFilterListe.get(position).coinName== svgList().svglist().get(a).coinCurrency){
                                Glide.with(holder.itemView).load(svgList().svglist().get(a).coinUrl).into(holder.itemView.imageView2)
                            }
                        }
                    }
                }
                //println(coinFilterList)
                //holder.itemView.textName.text=coinFilterListe[position].currency

                holder.itemView.bossList.setOnClickListener{
                    holder.itemView.doluList.visibility=View.VISIBLE
                    holder.itemView.bossList.visibility=View.INVISIBLE
                    var coinName= favListesi.get(position).coinName
                    val email = auth.currentUser?.email.toString()
                    val coinListem = db.collection("favList").document(email)
                        .collection("ikinciListem")
                    val data1 = hashMapOf(
                        "coinName" to coinName,
                    )
                    coinListem.document(coinName).set(data1)

                }

                holder.itemView.doluList.setOnClickListener {
                    holder.itemView.doluList.visibility=View.INVISIBLE
                    holder.itemView.bossList.visibility=View.VISIBLE
                    val email = auth.currentUser?.email.toString()
                    var coinName= coinFilterListe[position].coinName
                    db.collection("favList").document(email)
                        .collection("ikinciListem")
                        .document(coinName)
                        .delete()
                    /*coinFilterListe.remove(coinFilterListe[position])
                    notifyDataSetChanged()*/

                }
            }
        }catch (e:Exception){

        }

    }

    override fun getItemCount(): Int {
        return coinFilterListe.size
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch= p0.toString()
                if(charSearch.isEmpty()){
                    coinFilterListe=favListesi
                }else{
                    val resultList=ArrayList<favList>()
                    for(row in favListesi){
                        if(row.coinName.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))){
                            resultList.add(row)
                        }
                    }
                    coinFilterListe=resultList
                }
                val filterResults=FilterResults()
                filterResults.values=coinFilterListe
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                coinFilterListe=p1?.values as ArrayList<favList>
                notifyDataSetChanged()
            }

        }
    }
}
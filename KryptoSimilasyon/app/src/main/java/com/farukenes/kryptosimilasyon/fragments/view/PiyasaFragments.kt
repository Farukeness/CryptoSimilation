package com.farukenes.kryptosimilasyon.fragments.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farukenes.kryptosimilasyon.R
import com.farukenes.kryptosimilasyon.fragments.adapter.Adapter
import com.farukenes.kryptosimilasyon.fragments.adapter.favAdapter
import com.farukenes.kryptosimilasyon.fragments.model.CryptoModel
import com.farukenes.kryptosimilasyon.fragments.model.coinFile2
import com.farukenes.kryptosimilasyon.fragments.model.favList
import com.farukenes.kryptosimilasyon.fragments.serrvice.CryptoAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_piyasa_fragments.*
import kotlinx.android.synthetic.main.row_layout.*
import kotlinx.android.synthetic.main.row_layout_favlist.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PiyasaFragments : Fragment(), Adapter.Listener {
    private val BASE_URL = "https://api.nomics.com/v1/"
    private var cryptoModels: ArrayList<CryptoModel>?=null
    private var coinFilterListem:ArrayList<CryptoModel>?=null
    private var recylerViewAdapter : Adapter?=null
    private var recycylerViewAdapterFavList:favAdapter?=null
    var coinListesi: ArrayList<coinFile2>?=null
    var favList:ArrayList<favList>?=null
    private var job: Job?=null
    lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var starControl:Boolean?=false
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}")
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_piyasa_fragments, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
        favList= ArrayList<favList>()
        bos.setOnClickListener{bos(view) }
        dolu.setOnClickListener{dolu(view)}
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyler_view.layoutManager = layoutManager

        searchText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                recylerViewAdapter!!.filter.filter(p0)
                recycylerViewAdapterFavList!!.filter.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        /*search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                recylerViewAdapter!!.filter.filter(p0)
                recycylerViewAdapterFavList!!.filter.filter(p0)
                return false
            }

        })*/
        recyclerViewAdapterFav.layoutManager=LinearLayoutManager(requireContext())

        loadData()
        if(auth.currentUser!=null){
            favSetSnapShotListener()
        }

    }






    private fun loadData(){
        val retrofit= Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)
        job = CoroutineScope(Dispatchers.IO+ exceptionHandler).launch {
            val response = retrofit.getData()
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                     response.body()?.let {
                         cryptoModels = ArrayList(it)
                         cryptoModels?.let {
                             recylerViewAdapter= Adapter(it,this@PiyasaFragments, favList!!)
                             recyler_view.adapter=recylerViewAdapter

                             recycylerViewAdapterFavList= favAdapter(it,favList!!)
                             recyclerViewAdapterFav.adapter=recycylerViewAdapterFavList
                         }
                     }
                }
            }
        }
    }

    override fun onItemClick(cryptoModel: CryptoModel) {

    }


    fun bos(view: View){
        if(auth.currentUser!=null){
            dolu.visibility=View.VISIBLE
            bos.visibility=View.INVISIBLE
            recyler_view.visibility=View.INVISIBLE
            recyclerViewAdapterFav.visibility=View.VISIBLE
        }

    }

    fun dolu(view: View){
        if(auth.currentUser!=null){
            bos.visibility=View.VISIBLE
            dolu.visibility=View.INVISIBLE
            recyler_view.visibility=View.VISIBLE
            recyclerViewAdapterFav.visibility=View.INVISIBLE
        }

    }

    private fun favSetSnapShotListener(){
        val email = auth.currentUser?.email.toString()
        db.collection("favList").document(email).collection("ikinciListem").orderBy("coinRank",Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(value!=null){
                    if(!value.isEmpty){
                        val documents =value.documents
                        favList!!.clear()
                        for(document in documents){
                            val coinName = document.get("coinName").toString()
                            val rank= document.get("coinRank").toString().toInt()
                            val favlistesi=favList(coinName,rank)
                            favList!!.add(favlistesi)
                            //favlistesiyle crypto list eşleştirilecek burdan çıkan sonuçlar favadaptere
                            //aktarılacak
                        }
                        recycylerViewAdapterFavList?.notifyDataSetChanged()
                    }
                    recylerViewAdapter?.notifyDataSetChanged()

                }
                //recylerViewAdapter?.notifyItemRemoved(1)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }


}
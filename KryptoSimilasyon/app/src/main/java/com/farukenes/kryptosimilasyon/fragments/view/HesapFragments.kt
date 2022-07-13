package com.farukenes.kryptosimilasyon.fragments.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.farukenes.kryptosimilasyon.R
import com.farukenes.kryptosimilasyon.fragments.adapter.cuzdanAdapter
import com.farukenes.kryptosimilasyon.fragments.adapter.cuzdanListesi
import com.farukenes.kryptosimilasyon.fragments.dbroom.userDao
import com.farukenes.kryptosimilasyon.fragments.dbroom.userDatabase
import com.farukenes.kryptosimilasyon.fragments.model.CryptoModel
import com.farukenes.kryptosimilasyon.fragments.serrvice.CryptoAPI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_hesap_fragments.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList






class HesapFragments : Fragment() {
    private lateinit var dbroom: userDatabase
    private lateinit var userDao: userDao
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var coinListesi: ArrayList<cuzdanListesi>
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private lateinit var CuzdanAdapter: cuzdanAdapter
    private var recylerViewAdapter: cuzdanAdapter? = null
    private val BASE_URL = "https://api.nomics.com/v1/"
    private var toplam: Int? = null
    private var ytoplam: Int? = 0
    private var job: Job?=null
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
        return inflater.inflate(com.farukenes.kryptosimilasyon.R.layout.fragment_hesap_fragments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signOut.setOnClickListener { signOut(view) }
        result.setOnClickListener { result(view) }
        db = Firebase.firestore
        auth = Firebase.auth
        coinListesi = ArrayList<cuzdanListesi>()
        recyclerViewCüzdan.layoutManager = LinearLayoutManager(requireContext())
        val currentUser = auth.currentUser
        if (currentUser != null) {
            loadData()
            getCoinList()
            getData()
            /*GlobalScope.launch {
                //delay silip çalışcakmı kontrol et (çalışmadı)
                delay(1000)
                show()
            }*/
            //show()
        }else{
            val action = HesapFragmentsDirections.actionHesapFragmentsToLogginFragment2()
            Navigation.findNavController(view).navigate(action)
        }
    }

    fun signOut(view: View) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Çıkış Yapmak İstiyormusunuz")
            .setPositiveButton("Evet",
            DialogInterface.OnClickListener { dialogInterface, i ->
                auth.signOut()
                Toast.makeText(requireContext(), "Çıkış Başarılı", Toast.LENGTH_LONG).show()
                val action=HesapFragmentsDirections.actionHesapFragmentsToLogginFragment2()
                Navigation.findNavController(view).navigate(action)
            })
            .setNegativeButton("Hayır",
            DialogInterface.OnClickListener { dialogInterface, i ->
                Toast.makeText(requireContext(),"Çıkış yapılmaktan vazgeçildi",Toast.LENGTH_SHORT).show()
            })
        builder.create().show()




    }

    fun result(view: View) {
        show()
    }


    fun getData() {
        val email = auth.currentUser?.email.toString()
        db.collection("newUsers").document(email)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val email = it.get("Email")
                    val username = it.get("UserName")
                    var bakiye = it.get("bakiye").toString().toFloat()
                    textView.text = username.toString().capitalize()
                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    fun getCoinList() {
        val email = auth.currentUser?.email.toString()
        db.collection("coinListem").document(email).collection("ikinciListem")
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val documents = it.documents
                    for (document in documents) {
                        val coinName = document.get("coinName").toString()
                        val coinAdet = document.get("coinAdet").toString().toFloat()
                        val costPrice = document.get("costPrice").toString().toFloat()
                        /*println(coinName)
                        println(coinAdet)
                        println(costPrice)*/

                        val cuzdan = cuzdanListesi(coinName, coinAdet, costPrice)
                        coinListesi.add(cuzdan)
                    }
                    //CuzdanAdapter.notifyDataSetChanged()
                    //show()
                }
            }

    }

    fun loadData() {
        val retrofit = Retrofit.Builder()
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
                            CuzdanAdapter = cuzdanAdapter(coinListesi, it)
                            recyclerViewCüzdan.adapter = CuzdanAdapter
                        }
                    }
                }
            }

        }
        /*val service = retrofit.create(CryptoAPI::class.java)
        val call = service.getData()
        call.enqueue(object : Callback<List<CryptoModel>> {
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        cryptoModels = ArrayList(it)
                        for (cryptoModel: CryptoModel in cryptoModels!!) {
                            //println(cryptoModel.price)

                        }
                        cryptoModels?.let {
                            CuzdanAdapter = cuzdanAdapter(coinListesi, it)
                            recyclerViewCüzdan.adapter = CuzdanAdapter
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })*/
    }

    fun show() {
        ytoplam = 0
        for (position in 0..coinListesi.size - 2) {
            for (i in 0..cryptoModels!!.size - 1) {
                if (coinListesi.get(position).coinIsim == cryptoModels!!.get(i).currency) {
                    toplam = (cryptoModels!!.get(i).price.toString().toFloat() * coinListesi
                        .get(position)
                        .coinAdet.toString().toFloat()).toInt()
                }
            }
            ytoplam = toplam!! + ytoplam!!
        }
        textView.text=ytoplam.toString()


    }
    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}

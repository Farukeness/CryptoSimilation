package com.farukenes.kryptosimilasyon.fragments.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.farukenes.kryptosimilasyon.R
import com.farukenes.kryptosimilasyon.fragments.adapter.Adapter
import com.farukenes.kryptosimilasyon.fragments.adapter.cuzdanAdapter
import com.farukenes.kryptosimilasyon.fragments.model.CryptoModel
import com.farukenes.kryptosimilasyon.fragments.serrvice.CryptoAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_hesap_fragments.*
import kotlinx.android.synthetic.main.fragment_home_fragments.*
import kotlinx.android.synthetic.main.fragment_piyasa_fragments.*
import kotlinx.android.synthetic.main.fragment_trade_fragments.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragments : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val BASE_URL = "https://api.nomics.com/v1/"
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private var job: Job?=null
    var tether:Int?=0
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
        return inflater.inflate(R.layout.fragment_home_fragments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth
        val currentUser= auth.currentUser
        loadData()
        if(currentUser!=null){
            getTetherFromServer()
            getDataUserName()
        }


    }

    fun getTetherFromServer(){
        val email = auth.currentUser?.email.toString()
        db.collection("coinListem").document(email).collection("ikinciListem")
            .document("Tether")
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val bakiyeFromServer = it.get("coinAdet").toString().toFloat()
                    availableTextFromHesap.text = "${bakiyeFromServer!!.toInt()} $"

                }
            }
    }

    fun getDataUserName() {
        val email = auth.currentUser?.email.toString()
        db.collection("newUsers").document(email)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val email = it.get("Email")
                    val username = it.get("UserName")
                    userNameTextView.text = "Hoşgeldiniz:   " + username.toString().capitalize()
                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
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
                            btc.text=cryptoModels!!.get(0).price.toString()
                            eth.text=cryptoModels!!.get(1).price.toString()
                            bnb.text=cryptoModels!!.get(2).price.toString()
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
                        btc.text=cryptoModels!!.get(0).price.toString()
                        eth.text=cryptoModels!!.get(1).price.toString()
                        bnb.text=cryptoModels!!.get(2).price.toString()

                    }
                }
            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })*/
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }



}
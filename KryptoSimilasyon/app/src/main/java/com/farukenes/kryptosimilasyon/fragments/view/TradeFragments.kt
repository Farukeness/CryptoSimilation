package com.farukenes.kryptosimilasyon.fragments.view

import android.graphics.Color
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
import androidx.room.Room
import com.bumptech.glide.Glide
import com.farukenes.kryptosimilasyon.R
import com.farukenes.kryptosimilasyon.fragments.adapter.*
import com.farukenes.kryptosimilasyon.fragments.dbroom.userDao
import com.farukenes.kryptosimilasyon.fragments.dbroom.userDatabase
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
import kotlinx.android.synthetic.main.fragment_hesap_fragments.*
import kotlinx.android.synthetic.main.fragment_home_fragments.*
import kotlinx.android.synthetic.main.fragment_piyasa_fragments.*
import kotlinx.android.synthetic.main.fragment_trade_fragments.*
import kotlinx.android.synthetic.main.row_layout.*
import kotlinx.android.synthetic.main.row_layout.view.*
import kotlinx.android.synthetic.main.row_layout_favlist.view.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Logger.global


class TradeFragments : Fragment(),Adapter.Listener {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var listem: ArrayList<users>
    private lateinit var controlList: ArrayList<String>
    private var miktarKontrol: ArrayList<coinFile2>? = null
    private var butonKontrol = true
    private var miktarKontrol2 = true
    private var infoCurrency: String = ""
    private var infoPrice: Float? = null
    private var bakiyeFromServer: Float? = null
    private var emailFromServer: String = ""
    var favKontrol:Int?=0
    var userNameFromServer: String = ""
    private var coinNameFromServer: String = ""
    private var coinAdetFromServer: Float? = null
    private var costPriceFromServer: Float? = null
    private lateinit var coinListesi: ArrayList<modelToDelete>
    private val BASE_URL = "https://api.nomics.com/v1/"
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private var toplam: Int? = null
    private var ytoplam: Int? = 0
    private var btc:String =""
    private var btcPrice:Float?=null
    private var coinRank:Int?=null
    var favListe:ArrayList<String>?=null
    //private var recylerViewAdapter : tradeAdapter?=null
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
        return inflater.inflate(R.layout.fragment_trade_fragments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTexts()
        db = Firebase.firestore
        auth = Firebase.auth
        listem = ArrayList<users>()
        controlList = ArrayList<String>()
        miktarKontrol = ArrayList<coinFile2>()
        coinListesi= ArrayList<modelToDelete>()
        favListe=ArrayList<String>()
        try {
            val currentUser = auth.currentUser
            loadData()
            if (currentUser != null) {
                getTetherFromServer()
                getDataCoinList()
                addCoinList()
                getData()
                TextBoxsCheckChange()
                //favset çağrıldığında çökme yaşanıyor.Bir sayaç mantığı kullanılabilir
                //favSetSnapShotListenerr()


            }
        } catch (e: Exception) {

        }

        bir.setOnClickListener { bir(view) }
        iki.setOnClickListener { iki(view) }
        uc.setOnClickListener { uc(view) }
        dort.setOnClickListener { dort(view) }
        SatınAl.setOnClickListener { satınAl(view) }
        CoinSat.setOnClickListener { sat(view) }
        buy.setOnClickListener { buy(view) }
        sell.setOnClickListener { sell(view) }

    }

    fun TextBoxsCheckChange() {
        Miktar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (miktarKontrol2 == true) {
                    try {
                        var price = priceText.text.toString().toFloat()
                        val miktar = p0.toString().toFloatOrNull()
                        var usdtSon = miktar!! * price
                        usdt.setText("${usdtSon.toFloat()}")
                    } catch (e: Exception) {

                    }
                }
            }
        })


        usdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (miktarKontrol2 == false) {
                    try {
                        var price = priceText.text.toString().toFloat()
                        val usdt = p0.toString().toFloat()
                        var usdtSon = usdt!! / price
                        Miktar.setText("${usdtSon.toFloat()}")
                    } catch (e: Exception) {

                    }
                }
            }
        })
        Miktar.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                Miktar.setText("")
                miktarKontrol2 = true
            }
        }
        usdt.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                usdt.setText("")
                miktarKontrol2 = false
            }
        }
    }

    fun bir(view: View) {
        try {
            if (butonKontrol == true) {
                bir.setBackgroundColor(Color.parseColor("#FF03DAC5"))
                iki.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
                uc.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
                dort.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
                val hesap = (bakiyeFromServer.toString().toFloat() * 0.25).toInt()
                usdt.setText(hesap.toString())
                var miktar = (hesap / btcPrice!!).toFloat()
                Miktar.setText("${miktar.toFloat()}")
            } else {
                if (controlList.contains("BTC")==true || controlList.contains(infoCurrency)==true) {
                    bir.setBackgroundColor(Color.parseColor("#770000"))
                    iki.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
                    uc.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
                    dort.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
                    val email = auth.currentUser?.email.toString()
                    db.collection("coinListem").document(email).collection("ikinciListem")
                        .document(infoCurrency)
                        .get()
                        .addOnSuccessListener {
                            if (it != null) {
                                if (controlList.contains(infoCurrency) == true) {
                                    val coinAdetForbutton = it.get("coinAdet").toString().toFloat()
                                    Miktar.setText("${coinAdetForbutton!! * 0.25}")

                                }

                            }
                        }

                }else{

                }


            }
        } catch (e: Exception) {

        }


    }

    fun iki(view: View) {
        try {
            if (butonKontrol == true) {
                bir.setBackgroundColor(Color.parseColor("#FF03DAC5"))
                iki.setBackgroundColor(Color.parseColor("#FF03DAC5"))
                uc.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
                dort.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
                val hesap = (bakiyeFromServer.toString().toFloat() * 0.50).toInt()
                usdt.setText(hesap.toString())
                var miktar = (hesap / btcPrice!!).toFloat()
                Miktar.setText("${miktar.toFloat()}")
            } else {
                if (controlList.contains("BTC")==true || controlList.contains(infoCurrency)==true) {
                    bir.setBackgroundColor(Color.parseColor("#770000"))
                    iki.setBackgroundColor(Color.parseColor("#770000"))
                    uc.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
                    dort.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
                    val email = auth.currentUser?.email.toString()
                    db.collection("coinListem").document(email).collection("ikinciListem")
                        .document(infoCurrency)
                        .get()
                        .addOnSuccessListener {
                            if (it != null) {
                                if (controlList.contains(infoCurrency) == true) {
                                    val coinAdetForbutton = it.get("coinAdet").toString().toFloat()
                                    Miktar.setText("${coinAdetForbutton!! * 0.50}")

                                }

                            }
                        }

                }

            }
        } catch (e: Exception) {

        }


    }

    fun uc(view: View) {
        try {
            if (butonKontrol == true) {
                bir.setBackgroundColor(Color.parseColor("#FF03DAC5"))
                iki.setBackgroundColor(Color.parseColor("#FF03DAC5"))
                uc.setBackgroundColor(Color.parseColor("#FF03DAC5"))
                dort.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
                val hesap = (bakiyeFromServer.toString().toFloat() * 0.75).toInt()
                usdt.setText(hesap.toString())
                var miktar = (hesap / btcPrice!!).toFloat()
                Miktar.setText("${miktar.toFloat()}")
            } else {
                if (controlList.contains("BTC")==true || controlList.contains(infoCurrency)==true) {
                    bir.setBackgroundColor(Color.parseColor("#770000"))
                    iki.setBackgroundColor(Color.parseColor("#770000"))
                    uc.setBackgroundColor(Color.parseColor("#770000"))
                    dort.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
                    val email = auth.currentUser?.email.toString()
                    db.collection("coinListem").document(email).collection("ikinciListem")
                        .document(infoCurrency)
                        .get()
                        .addOnSuccessListener {
                            if (it != null) {
                                if (controlList.contains(infoCurrency) == true) {
                                    val coinAdetForbutton = it.get("coinAdet").toString().toFloat()
                                    Miktar.setText("${coinAdetForbutton!! * 0.75}")

                                }

                            }
                        }
                }
            }
        } catch (e: Exception) {

        }


    }

    fun dort(view: View) {
        try {
            if (butonKontrol == true) {
                bir.setBackgroundColor(Color.parseColor("#FF03DAC5"))
                iki.setBackgroundColor(Color.parseColor("#FF03DAC5"))
                uc.setBackgroundColor(Color.parseColor("#FF03DAC5"))
                dort.setBackgroundColor(Color.parseColor("#FF03DAC5"))
                val hesap = (bakiyeFromServer.toString().toFloat()).toInt()
                usdt.setText(hesap.toString())
                val miktar = (hesap / btcPrice!!).toFloat()
                Miktar.setText("${miktar.toFloat()}")

            } else {
                if (controlList.contains("BTC")==true || controlList.contains(infoCurrency)==true) {
                    bir.setBackgroundColor(Color.parseColor("#770000"))
                    iki.setBackgroundColor(Color.parseColor("#770000"))
                    uc.setBackgroundColor(Color.parseColor("#770000"))
                    dort.setBackgroundColor(Color.parseColor("#770000"))
                    val email = auth.currentUser?.email.toString()
                    db.collection("coinListem").document(email).collection("ikinciListem")
                        .document(infoCurrency)
                        .get()
                        .addOnSuccessListener {
                            if (it != null) {
                                if (controlList.contains(infoCurrency) == true) {
                                    val coinAdetForbutton = it.get("coinAdet").toString().toFloat()
                                    Miktar.setText("${coinAdetForbutton!!}")

                                }

                            }
                        }
                }
            }
        } catch (e: Exception) {

        }


    }

    fun satınAl(view: View) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (usdt.text.toString().equals("") || Miktar.text.toString().equals("")) {
                Toast.makeText(
                    requireContext(),
                    "Coin adedi veya Usdt miktarı girin!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                var inputUsdt = usdt.text.toString().toFloat()
                val email = auth.currentUser?.email.toString()
                val bakiyem = bakiyeFromServer.toString().toFloat()
                if (bakiyem >= inputUsdt) {
                    var yeniBakiye = bakiyem - inputUsdt
                    Toast.makeText(
                        requireContext(),
                        "Satın Alma Başarılı. Kalan Bakiye:   ${yeniBakiye}",
                        Toast.LENGTH_LONG
                    ).show()
                    available.text = yeniBakiye.toString()
                    val bbakiyeFromServer=getTetherFromServer()

                    arguments?.let {
                        val info = TradeFragmentsArgs.fromBundle(it)
                        var costPrice = info.price
                        var coinName = info.currency
                        var coinAdet = Miktar.text.toString().toFloat()
                        if(coinName==cryptoModels!!.get(0).currency){
                            costPrice=cryptoModels!!.get(0).price.toString().toFloat()
                        }
                        /*db.collection("newUsers").document(email)
                            .update("bakiye", yeniBakiye)*/

                        db.collection("coinListem").document(email)
                            .collection("ikinciListem")
                            .document("Tether")
                            .update("coinAdet", yeniBakiye)
                        usdt.setText("")
                        Miktar.setText("")
                        if (controlList.contains(coinName) == true) {
                            db.collection("coinListem").document(email)
                                .collection("ikinciListem").document(coinName)
                                .get()
                                .addOnSuccessListener {
                                    if (it != null) {
                                        var coinAdetFromServer =
                                            it.get("coinAdet").toString().toFloat()
                                        var yeniAdet = coinAdet + coinAdetFromServer
                                        db.collection("coinListem").document(email)
                                            .collection("ikinciListem")
                                            .document(coinName)
                                            .update("coinAdet", yeniAdet,"costPrice",costPrice)

                                    }
                                }
                        } else {
                            val coinListem = db.collection("coinListem").document(email)
                                .collection("ikinciListem")
                            val data1 = hashMapOf(
                                "coinName" to coinName,
                                "costPrice" to costPrice,
                                "coinAdet" to coinAdet,
                                "currentPrice" to costPrice,
                                "degeri" to inputUsdt
                            )
                            coinListem.document(coinName).set(data1)
                        }


                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Bakiyeniz yeterli değil!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Lütfen giriş yapınız yada kayıt olunuz",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun setTexts() {
        arguments?.let {
            val info = TradeFragmentsArgs.fromBundle(it)
            infoCurrency = info.currency
            infoPrice = info.price
            favKontrol=info.favKontrol
            println(favKontrol)
            textBox.text = info.currency + " USDT"
            Miktar.hint = "Miktar " + info.currency
            CoinSat.text = info.currency + " Sat"
            SatınAl.text = info.currency + " Al"

        }
    }

    fun sat(view: View) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (usdt.text.toString().equals("") || Miktar.text.toString().equals("")) {
                Toast.makeText(
                    requireContext(),
                    "Coin adedi veya Usdt miktarı girin!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                var usdtText = usdt.text.toString().toFloat()
                var adetText = Miktar.text.toString().toFloat()
                var coinName = infoCurrency
                val email = auth.currentUser?.email.toString()
                db.collection("coinListem").document(email).collection("ikinciListem")
                    .document(infoCurrency)
                    .get()
                    .addOnSuccessListener {
                        if (it != null) {
                            if (controlList.contains(infoCurrency) == true) {
                                var coinAdetServer = it.get("coinAdet").toString().toFloat()
                                coinNameFromServer = it.get("coinName").toString()
                                costPriceFromServer = it.get("costPrice").toString().toFloat()
                                if (controlList.contains(coinName)==true) {
                                    var coinAdet = coinAdetServer.toString().toFloat()
                                    if (coinAdet >= adetText) {
                                        var yeniBakiye =
                                            bakiyeFromServer.toString().toFloat()!! + usdtText.toString().toFloat()
                                        available.text = yeniBakiye.toInt().toString() + " $"
                                        usdt.setText("")
                                        Miktar.setText("")

                                        var yeniAdet = coinAdet - adetText
                                        if(yeniAdet.toString()=="0.0"){
                                            db.collection("coinListem").document(emailFromServer)
                                                .collection("ikinciListem")
                                                .document(coinName)
                                                .delete()

                                            db.collection("coinListem").document(emailFromServer)
                                                .collection("ikinciListem").document(coinName)
                                                .update("coinAdet", yeniAdet)

                                            db.collection("coinListem").document(emailFromServer)
                                                .collection("ikinciListem").document("Tether")
                                                .update("coinAdet", yeniBakiye)

                                            /*db.collection("newUsers").document(emailFromServer)
                                                .update("bakiye", yeniBakiye)*/

                                            Toast.makeText(
                                                requireContext(),
                                                "Satış İşleminiz Başarılı Kalan Adetini: ${yeniAdet}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }else{
                                            db.collection("coinListem").document(emailFromServer)
                                                .collection("ikinciListem").document(coinName)
                                                .update("coinAdet", yeniAdet)

                                            db.collection("coinListem").document(emailFromServer)
                                                .collection("ikinciListem").document("Tether")
                                                .update("coinAdet", yeniBakiye)
                                            Toast.makeText(
                                                requireContext(),
                                                "Satış İşleminiz Başarılı Kalan Adetini: ${yeniAdet}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                        controlList.remove(coinName)


                                        /*for (i in 0..coinListesi.size -1){
                                            if(coinListesi.get(i).coinAdet.toString()=="0.0"){
                                                db.collection("coinListem").document(emailFromServer)
                                                    .collection("ikinciListem")
                                                    .document(coinListesi.get(i).coinIsim)
                                                    .delete()
                                            }
                                        }*/

                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Bakiyenizdeki adetten yüksek adet girdiniz!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Portföyünüzde seçtiğiniz coin bulunmamaktadır!",
                                        Toast.LENGTH_LONG
                                    ).show()

                                }

                            }

                        }
                    }


            }

        } else {
            Toast.makeText(
                requireContext(),
                "Lütfen giriş yapınız yada kayıt olunuz",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    fun buy(view: View) {
        bir.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
        iki.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
        uc.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
        dort.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
        Miktar.setText("")
        usdt.setText("")
        butonKontrol = true
        SatınAl.visibility = View.VISIBLE
        CoinSat.visibility = View.INVISIBLE
        buy.setBackgroundColor(Color.parseColor("#FF03DAC5"))
        sell.setBackgroundColor(Color.parseColor("#2E2E2E"))
    }

    fun sell(view: View) {
        butonKontrol = false
        bir.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
        iki.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
        uc.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
        dort.setBackgroundColor(Color.parseColor("#FFD5CFCF"))
        Miktar.setText("")
        usdt.setText("")
        SatınAl.visibility = View.INVISIBLE
        CoinSat.visibility = View.VISIBLE
        sell.setBackgroundColor(Color.parseColor("#770000"))
        buy.setBackgroundColor(Color.parseColor("#2E2E2E"))
    }


    fun getData() {
        val email = auth.currentUser?.email.toString()
        db.collection("newUsers").document(email)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    emailFromServer = it.get("Email") as String
                    userNameFromServer = it.get("UserName") as String
                    //bakiyeFromServer = it.get("bakiye").toString().toFloat()
                    //available.text = "${bakiyeFromServer!!.toInt()} $"
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    fun getDataCoinList() {
        val email = auth.currentUser?.email.toString()
        db.collection("coinListem").document(email).collection("ikinciListem")
            .document(infoCurrency)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    if (controlList.contains(infoCurrency) == true) {
                        coinAdetFromServer = it.get("coinAdet").toString().toFloat()
                        coinNameFromServer = it.get("coinName").toString()
                        costPriceFromServer = it.get("costPrice").toString().toFloat()

                    }

                }
            }

    }

    fun getTetherFromServer(){
        val email = auth.currentUser?.email.toString()
        db.collection("coinListem").document(email).collection("ikinciListem")
            .document("Tether")
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    bakiyeFromServer = it.get("coinAdet").toString().toFloat()
                    available.text = "${bakiyeFromServer!!.toInt()} $"


                }
            }

    }

    fun addCoinList() {
        val email = auth.currentUser?.email.toString()
        db.collection("coinListem").document(email).collection("ikinciListem")
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val documents = it.documents
                    for (document in documents) {
                        val coinName = document.get("coinName").toString()
                        val coinAdet= document.get("coinAdet").toString().toFloat()
                        val costPrice= document.get("costPrice").toString().toFloat()
                        val currentPrice = document.get("currentPrice").toString().toFloat()
                        /*println(coinName)
                        println(coinAdet)
                        println(costPrice)*/
                        controlList.add(coinName)
                        val cuzdan = modelToDelete(coinName,coinAdet,costPrice,currentPrice)
                        coinListesi.add(cuzdan)
                    }
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
                            for (i in 0..cryptoModels!!.size-1){
                                if(cryptoModels!!.get(i).currency==infoCurrency){
                                    priceText.text=cryptoModels!!.get(i).price.toString().substring(0,8)+" $"
                                    //maxSupply.text=cryptoModels!!.get(i).max_supply.toString()
                                    //circulatingSupply.text=cryptoModels!!.get(i).circulating_supply.toString()
                                    marketCap.text="Piyasa Değeri: "+cryptoModels!!.get(i).market_cap.toString()+" $"
                                    btcPrice=cryptoModels!!.get(i).price.toString().toFloat()
                                    coinRank=cryptoModels!!.get(i).rank.toInt()
                                    //println(cryptoModels!!.get(i).priceChange)
                                    var priceChanged=cryptoModels!!.get(i).priceChange.price_change
                                    if(Math.signum(priceChanged.toString().toFloat()).toString()=="1.0"){
                                        val ilkd = cryptoModels!!.get(i).price.toString().toFloat()-priceChanged.toString().toFloat()
                                        val sond=cryptoModels!!.get(i).price
                                        val degisim= sond.toString().toFloat()-ilkd.toString().toFloat()
                                        textYuzdelik.setTextColor(Color.parseColor("#FF00BC05"))
                                        textYuzdelik.text="+% "+((degisim.toString().toFloat()/ilkd.toString().toFloat())*100).toString().substring(0,3)
                                    }else if (Math.signum(priceChanged.toString().toFloat()).toString()=="-1.0"){
                                        val ilkd = cryptoModels!!.get(i).price.toString().toFloat()+priceChanged.toString().toFloat()
                                        val sond=cryptoModels!!.get(i).price
                                        val degisim= sond.toString().toFloat()-ilkd.toString().toFloat()
                                        textYuzdelik.setTextColor(Color.parseColor("#FFCF0000"))
                                        textYuzdelik.text="-% "+((degisim.toString().toFloat()/ilkd.toString().toFloat())*100).toString().substring(0,3)
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }



    override fun onItemClick(cryptoModel: CryptoModel) {

    }

    fun show() {
        ytoplam = 0
        for (position in 0..coinListesi.size - 1) {
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

    fun bosTrade(view: View){
        try {
            if(auth.currentUser!=null){
                var coinName= infoCurrency
                val email = auth.currentUser?.email.toString()
                val coinListem = db.collection("favList").document(email)
                    .collection("ikinciListem")
                val data1 = hashMapOf(
                    "coinName" to coinName,
                    "coinRank" to coinRank
                )
                coinListem.document(coinName).set(data1)
            }
        }catch (e:Exception){

        }

    }

    fun doluTrade(view: View){
        try {
            if(auth.currentUser!=null){

                val email = auth.currentUser?.email.toString()
                var coinName= infoCurrency
                db.collection("favList").document(email)
                    .collection("ikinciListem")
                    .document(coinName)
                    .delete()

            }
        }catch (e:Exception){

        }

    }

    fun favSetSnapShotListenerr(){
        try {
            val email = auth.currentUser?.email.toString()
            db.collection("favList").document(email).collection("ikinciListem").addSnapshotListener { value, error ->
                if(error!=null){
                    Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
                }else{
                    if(value!=null){
                        if(!value.isEmpty){
                            //burda job kullanarak sıralı bir şekile sokulabilir. aynı zamanda arka planda çalışması gerekiyor.
                            val documents =value.documents
                            for(document in documents){
                                val coinName = document.get("coinName").toString()
                                favListe!!.add(coinName)
                            }
                            /*for(i in 0..favListe!!.size-1){
                                if(favListe!!.get(i)==infoCurrency){
                                    doluTrade.visibility=View.VISIBLE
                                    bosTrade.visibility=View.INVISIBLE
                                    break
                                    //Glide.with(this@TradeFragments).load("https://www.nicepng.com/png/full/4-41674_review-star-transparent-background-star-clipart.png").into(bosTrade)
                                }
                            }*/





                        }
                    }
                }
            }
        }catch (e:Exception){

        }

    }

    fun favKontrolcusuFromPiyasa(){
        if(favKontrol==0){

        }else if(favKontrol==1){

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }





}










package com.farukenes.kryptosimilasyon.fragments.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.farukenes.kryptosimilasyon.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_hesap_fragments.*
import kotlinx.android.synthetic.main.fragment_loggin.*
import java.text.SimpleDateFormat
import java.util.*


class LogginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loggin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth

        logginButton.setOnClickListener { logginButton(view) }
        yeniKayitText.setOnClickListener { yeniKayitText(view) }
        kayitOlButton.setOnClickListener { kayitOlButton(view) }
        girisYapText.setOnClickListener { girisYapText(view) }
    }

    fun logginButton(view:View){
        val email = emailText.text.toString()
        val password = passwordText.text.toString()
        if (email.equals("") || password.equals("")) {
            Toast.makeText(requireContext(), "Boş bırakılamaz", Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val action = LogginFragmentDirections.actionLogginFragmentToHesapFragments2()
                Navigation.findNavController(view).navigate(action)

            }

        }
    }

    fun yeniKayitText(view: View){
        kayitOlButton.visibility=View.VISIBLE
        userNameText.visibility=View.VISIBLE
        logginButton.visibility=View.INVISIBLE
        yeniKayitText.visibility=View.INVISIBLE
        textView4.visibility=View.INVISIBLE
        textView5.visibility=View.VISIBLE
        girisYapText.visibility=View.VISIBLE
    }

    fun kayitOlButton(view: View){
        val email = emailText.text.toString().lowercase()
        val password = passwordText.text.toString()
        val username = userNameText.text.toString()

        if (email.equals("") || password.equals("")) {
            Toast.makeText(
                requireContext(),
                "Email yada Password Boş Bırakılamaz",
                Toast.LENGTH_LONG
            ).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    //getData()
                    if (auth.currentUser != null) {
                        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
                        val currentDateAndTime: String = simpleDateFormat.format(Date())
                        val cities = db.collection("newUsers")
                        val bakiyem = 3000
                        val data1 = hashMapOf(
                            "UserName" to username,
                            "Email" to email,
                            "bakiye" to bakiyem,
                            "kayitzamani" to currentDateAndTime
                        )

                        cities.document(email).set(data1)
                        val coinListem = db.collection("coinListem").document(email)
                            .collection("ikinciListem")
                        val data2 = hashMapOf(
                            "coinName" to "USDT",
                            "costPrice" to "0",
                            "coinAdet" to bakiyem,
                            "currentPrice" to 1.0,
                            "degeri" to bakiyem

                        )
                        coinListem.document("Tether").set(data2)

                        val favKontrol = db.collection("favList").document(email)
                            .collection("favKontrol")
                        val data3 = hashMapOf(
                            "favKontrol" to "false",

                        )
                        favKontrol.document("kontrolfav").set(data3)
                        val action= LogginFragmentDirections.actionLogginFragmentToHesapFragments2()
                        Navigation.findNavController(view).navigate(action)


                    } else {
                        Toast.makeText(requireContext(), "Kaydetme Başarısız", Toast.LENGTH_LONG)
                            .show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }
    }

    fun girisYapText(view:View){
        kayitOlButton.visibility=View.INVISIBLE
        userNameText.visibility=View.INVISIBLE
        logginButton.visibility=View.VISIBLE
        yeniKayitText.visibility=View.VISIBLE
        textView4.visibility=View.VISIBLE
        textView5.visibility=View.INVISIBLE
        girisYapText.visibility=View.INVISIBLE

    }

}
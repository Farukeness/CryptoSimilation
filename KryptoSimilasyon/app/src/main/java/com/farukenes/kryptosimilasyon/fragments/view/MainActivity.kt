package com.farukenes.kryptosimilasyon.fragments.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.farukenes.kryptosimilasyon.R
import com.farukenes.kryptosimilasyon.fragments.dbroom.userDao
import com.farukenes.kryptosimilasyon.fragments.dbroom.userDatabase
import com.farukenes.kryptosimilasyon.fragments.model.CryptoModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private val BASE_URL = "https://api.nomics.com/v1/"
    private var cryptoModels: ArrayList<CryptoModel>?=null
    private lateinit var dbroom: userDatabase
    private lateinit var userDao: userDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        val savedState = supportFragmentManager.saveFragmentInstanceState(navHostFragment)
        navHostFragment.setInitialSavedState(savedState)
        bottomNavigationView.setupWithNavController(navController)*/

        val navController = findNavController(R.id.container_fragment)
        val navView: BottomNavigationView = bottom_navigation_view
        navView.setupWithNavController(navController)



        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragments, R.id.piyasaFragments, R.id.tradeFragments,R.id.hesapFragments
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)*/



    }
    fun getirList():ArrayList<String>{
        var bosListe= ArrayList<String>()
        bosListe.add("Faruk")
        return bosListe
    }


}
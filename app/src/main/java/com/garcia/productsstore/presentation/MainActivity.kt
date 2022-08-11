package com.garcia.productsstore.presentation

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.garcia.productsstore.R
import com.garcia.productsstore.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navHostFragment: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView)?.findNavController()
        navHostFragment?.let {
            NavigationUI.setupActionBarWithNavController(this, it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navHostFragment?.let {
            return it.navigateUp()
        }
        return super.onSupportNavigateUp()
    }
}
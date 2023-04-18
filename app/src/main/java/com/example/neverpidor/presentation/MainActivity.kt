package com.example.neverpidor.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.neverpidor.R
import com.example.neverpidor.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!
    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: MainViewModel by viewModels()
    private lateinit var exitButton: ImageView
    private lateinit var header: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        setupNavViewHeader()
        observeNavViewState()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.listFragment,
                R.id.loginFragment,
                R.id.registerFragment,
                R.id.profileFragment
            ), binding.drawerLayout
        )
        binding.navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupNavViewHeader() {
        header = binding.navView.getHeaderView(0)
        exitButton = header.findViewById(R.id.exitIcon)
        exitButton.setOnClickListener {
            viewModel.logout()
            val fragment = navController.currentDestination?.id ?: 0
            Log.e("FRAG", fragment.toString())
            if (fragment != R.id.listFragment) {
                navController.navigate(R.id.listFragment)
            }
        }
    }

    private fun observeNavViewState() {
        lifecycleScope.launch {
            viewModel.userName.collectLatest {
                header.findViewById<TextView>(R.id.userName).text = it
                binding.navView.menu.findItem(R.id.profileFragment).isEnabled = it != ""
                binding.navView.menu.findItem(R.id.loginFragment).isEnabled = it == ""
                binding.navView.menu.findItem(R.id.registerFragment).isEnabled = it == ""
                exitButton.isGone = it == ""
                header.findViewById<TextView>(R.id.exitText).isGone = it == ""
            }
        }
    }
}
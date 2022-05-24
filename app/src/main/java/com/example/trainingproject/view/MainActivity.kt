package com.example.trainingproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.trainingproject.controller.MainController
import com.example.trainingproject.util.Constants.INTENT_EXTRA_LOCATION
import com.example.trainingproject.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainController: MainController
    private lateinit var locationAdapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainController = MainController(this)
        locationAdapter = LocationAdapter {
            val intent = Intent(this, LocationDetailsActivity::class.java)
            intent.putExtra(INTENT_EXTRA_LOCATION, it)
            startActivity(intent)
        }
        binding.recycler.adapter = locationAdapter

        binding.swipe.setOnRefreshListener {
            locationAdapter.refresh()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainController.getLocations.collect {
                    locationAdapter.submitData(it)
                }
            }
        }

        lifecycleScope.launch {
            locationAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipe.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }
    }
}
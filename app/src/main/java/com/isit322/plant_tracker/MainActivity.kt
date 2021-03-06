package com.isit322.plant_tracker

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.isit322.artworklist.adapters.AdapterRecycler
import com.isit322.artworklist.data.PlantItem
import com.isit322.artworklist.ui.PlantViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var plantViewModel: PlantViewModel
    lateinit var adapterRecyclerView: AdapterRecycler
    var plantList: List<PlantItem>? = ArrayList()

    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                //precise location access granted.
            }
            permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                //only approximate location access granted.
            } else -> {
                //No location access granted
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*locationPermissionRequest.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION))*/


        val mapButton = findViewById<Button>(R.id.mapBtn)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        val enterPlant = findViewById<Button>(R.id.enterPlant)
        enterPlant.setOnClickListener() {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        init()
    }

    private fun init() {
        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)
        adapterRecyclerView = AdapterRecycler(plantList, this)
        recycler_view.adapter = adapterRecyclerView
        recycler_view.layoutManager = LinearLayoutManager(this)

        startBtn.setOnClickListener {
            progress_bar.visibility = View.VISIBLE

            plantViewModel.getArtwork(this)
            plantViewModel.plantResponse.observe(this) {
                if (!it.isEmpty()) {
                    progress_bar.visibility = View.GONE
                    linear_layout_recycler_view.visibility = View.VISIBLE
                    plantList = it
                    adapterRecyclerView.setData(plantList)
                }
                else {
                    progress_bar.visibility = View.GONE
                }
            }
        }
    }
}
package com.example.trainingproject.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.example.trainingproject.model.Location
import com.example.trainingproject.util.Constants.INTENT_EXTRA_LOCATION
import com.example.trainingproject.R
import com.example.trainingproject.databinding.ActivityLocationDetailsBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityLocationDetailsBinding
    private lateinit var location: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        location = intent.getParcelableExtra(INTENT_EXTRA_LOCATION) ?: Location()

        with(binding) {
            name.text = location.name
            address.text = location.address
            if (location.photo.isNotEmpty()) logo.load(location.photo)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(35.00116, 135.7681)
        googleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(location.name)
                .snippet(location.address)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }
}
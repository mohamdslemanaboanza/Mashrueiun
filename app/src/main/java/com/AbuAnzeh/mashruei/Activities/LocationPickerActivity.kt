package com.AbuAnzeh.mashruei.Activities

import android.Manifest
import android.app.Activity
import android.graphics.Point
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.AbuAnzeh.mashruei.Fragment.CartFragment
import com.AbuAnzeh.mashruei.HelperClass.MyLocation
import com.AbuAnzeh.mashruei.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import java.util.*

class LocationPickerActivity : AppCompatActivity(), OnMapReadyCallback {


    lateinit var btnContinue: Button
    lateinit var map: FragmentContainerView
    private lateinit var mMap: GoogleMap
    private lateinit var tvLocationName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)
        btnContinue = findViewById(R.id.btnContinue)
        map = findViewById(R.id.map)
        tvLocationName = findViewById(R.id.tvLocationName)

        requestPermissions()
        setUpMap()
        setUpListeners()

    }

    private fun requestPermissions() {
        val permReqLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all {
                    it.value == true
                }
                if (granted) {
                    getUserLocation()
                }
            }

        permReqLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    private fun setUpListeners() {
        btnContinue.setOnClickListener {
            onNextClicked()
        }

    }

    private fun onNextClicked() {
        onBackPressed()
    }

    private fun getUserLocation() {
        val locationResult: MyLocation.LocationResult = object : MyLocation.LocationResult() {
            override fun gotLocation(location: Location?) {
                location?.let {
                    CreateStoreActivity.latitude = location.latitude
                    CreateStoreActivity.longitude = location.longitude
                    CartFragment.longitude = location.longitude
                    CartFragment.latitude = location.latitude
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                location.latitude,
                                location.longitude
                            ), 16f
                        )
                    )
                }

            }
        }
        val myLocation = MyLocation()
        myLocation.getLocation(this, locationResult)
    }

    private fun getLatLng(): LatLng {
        val array = IntArray(2)
        map.getLocationOnScreen(array)
        val mapX = array[0]
        val mapY = array[1]

        val mapWidth = map?.width ?: 0
        val mapHeight = map?.height ?: 0
        val mapCenterX = (mapX + mapWidth / 2)
        val mapCenterY = (mapY + mapHeight / 2)
        return mMap.projection.fromScreenLocation(Point(mapCenterX, mapCenterY))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.setOnCameraIdleListener {
            try {
                tvLocationName.text = getAreaInfo(getLatLng())
                mMap.clear()
            } catch (e: Exception) {
            }
        }
    }

    private fun setUpMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }




        fun getAreaInfo(latLng: LatLng): String {

            val gcd = Geocoder(this, Locale.forLanguageTag("ar"))
            val addresses: List<Address> = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (addresses.isNotEmpty()) {
                return addresses[0].getAddressLine(0)
            }

            return "Location UnKnown"
        }

}
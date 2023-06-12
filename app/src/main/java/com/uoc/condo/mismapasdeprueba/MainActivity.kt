package com.uoc.condo.mismapasdeprueba

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback, OnMyLocationButtonClickListener, OnMyLocationClickListener {
    private lateinit var map: GoogleMap


    companion object{
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createFragment()
    }

    private fun createFragment() {

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
         mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap?) {

        if (googleMap != null) {

            map = googleMap
            createMarket(28.043893, -16.539329, "Portugal")
            createMarket(-22.971963349150883, -43.18427538360889, "Copacabana, Brazil")
            map.setOnMyLocationButtonClickListener(this)
            map.setOnMyLocationClickListener(this)
            enableLoaction()
        }

    }

    private fun createMarket(lat: Double, lng: Double, title : String  ) {

       val coordinates = LatLng(lat, lng)
       val marker:MarkerOptions = MarkerOptions().position(coordinates).title(title)
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),4000, null
        )
    }


    private fun isLocationPermissionGranted() =
     ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED


    private fun enableLoaction(){
        if (!::map.isInitialized) return

        if (isLocationPermissionGranted()){
            map.isMyLocationEnabled = true
        } else {
            requestLocation()
        }

    }

    private fun requestLocation() {
       if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
           Toast.makeText(this, "Vaya a ajustes y acepte los permisos", Toast.LENGTH_LONG).show()
       }
        else {
           ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
               REQUEST_CODE_LOCATION)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true
            } else {
                Toast.makeText(this, "Para activar la localización ve a ajustes y activar los permisos", Toast.LENGTH_LONG).show()
            }
            else -> {
                //Es otro permiso
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::map.isInitialized) return
        if (!isLocationPermissionGranted()){
            map.isMyLocationEnabled = false
            Toast.makeText(this, "Para activar la localización ve a ajustes y activar los permisos", Toast.LENGTH_LONG).show()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Boton pulsado", Toast.LENGTH_LONG).show()

        return false

    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Estas en + ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_LONG).show()
    }
}
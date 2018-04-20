package sample.network.rahul.android_weather_app.gps

import android.arch.lifecycle.LiveData
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest





class CurrentLocationListener(appContext: Context) : LiveData<Location>(),com.google.android.gms.location.LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    override fun onLocationChanged(location: Location?) {
        value = location;
    }

    override fun onConnected(p0: Bundle?) {

        val lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)

        if (lastLocation != null) {
            value = lastLocation
        }
        if (hasActiveObservers() && googleApiClient.isConnected) {
            //            LocationRequest locationRequest = LocationHelper.createRequest();
            val locationRequest = LocationRequest.create()
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private lateinit var googleApiClient: GoogleApiClient

    init {
        buildGoogleApiClient(appContext)
    }

    companion object {
        @Synchronized
        fun getInstance(context: Context): CurrentLocationListener {
            return CurrentLocationListener(context)
        }
    }

    @Synchronized
    private fun buildGoogleApiClient(appContext: Context) {
        googleApiClient = GoogleApiClient.Builder(appContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }

    override fun onActive() {
        super.onActive()
        googleApiClient.connect()
    }

    override fun onInactive() {
        if (googleApiClient.isConnected) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
            googleApiClient.disconnect()
        }
    }
}
package sample.network.rahul.android_weather_app.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.location.Location
import sample.network.rahul.android_weather_app.datasource.data.WeatherResponse
import sample.network.rahul.android_weather_app.datasource.remote.WeatherClient

public class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private var weatherResponse: LiveData<WeatherResponse>
    private  var weatherClient: WeatherClient
    private var location: MutableLiveData<Location> = MutableLiveData()
    private val query = MutableLiveData<String>()

    init {
        weatherClient = WeatherClient.getInstance()
        weatherResponse = Transformations.switchMap(location) { loc ->
            if (loc == null || loc.latitude == 0.0 || loc.longitude == 0.0) {
                return@switchMap MutableLiveData<WeatherResponse>()
            } else {
                return@switchMap weatherClient.getWeather(loc)
            }
        }
    }

    fun getWeather(): LiveData<WeatherResponse> {
        return weatherResponse
    }

    fun refetchWeather(location: Location){
        this.location.value = location
    }


}
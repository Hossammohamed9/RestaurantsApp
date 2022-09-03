package com.example.task.ui.restaurarnts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.data.models.DataX
import com.example.task.data.models.Sliders
import com.example.task.data.repository.Repository
import com.example.task.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import javax.inject.Inject

const val TAG = "RestaurantViewModelVM"

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val repository: Repository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private var _restaurants = MutableLiveData<List<DataX>>()
    val restaurants: LiveData<List<DataX>>
        get() = _restaurants

    private var _ads = MutableLiveData<List<Sliders>>()
    val ads: LiveData<List<Sliders>>
        get() = _ads

    var tempList = mutableListOf<DataX>()


    var isName = false
    var isDelev = false
    var isFee = false
    var isRate = false



    init {
        getAllRestaurants()
        getSliderAds()
    }


    private fun getAllRestaurants(){
        viewModelScope.launch(dispatcher.io) {
            try {
                val response = repository.getSellingRestaurants()
                if (response.isSuccessful){
                    _restaurants.postValue(response.body()?.GetNearestBranche?.data)
                }
            }catch (e: Exception){
                Log.e(TAG, "getMostSellingRestaurants Method: $e")
            }
        }

    }

    private fun getSliderAds(){
        viewModelScope.launch(dispatcher.io) {
            try {
                val response = repository.getAds()
                if (response.isSuccessful){
                    _ads.postValue(response.body()?.flatMap { it.AdsSpacesprice.map { it.sliders } })
                }
            }catch (e: Exception){
                Log.e(TAG, "getSliderAds Method: $e")
            }
        }
    }

    fun search(query: String){
        viewModelScope.launch {
            for (i in _restaurants.value!!){
                if (i.name.lowercase(Locale.getDefault()).contains(query)){
                    tempList.add(i)
                }
            }
        }
    }

}
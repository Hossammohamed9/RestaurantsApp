package com.example.task.ui.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.data.models.Category
import com.example.task.data.repository.Repository
import com.example.task.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

const val TAG = "CategoryViewModelVM"

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: Repository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private var _category = MutableLiveData<List<Category>>()
    val category: LiveData<List<Category>>
        get() = _category

    private var _navigateToRestaurantFragment = MutableLiveData<Category?>()
    val navigateToRestaurantFragment : LiveData<Category?>
        get() = _navigateToRestaurantFragment

    init {
        getAllCategories()
    }

    private fun getAllCategories(){
        viewModelScope.launch(dispatcher.io) {
            try {
                val response = repository.getCategory()
                if (response.isSuccessful){
                    _category.postValue(response.body())
                }
            }catch (e: Exception){
                Log.e(TAG, "getAllCategories Method: $e")
            }
        }
    }

    fun displayRestaurantFragment(category: Category){
        _navigateToRestaurantFragment.value = category
    }

    fun displayRestaurantFragmentComplete()
    {
        _navigateToRestaurantFragment.value = null
    }
}

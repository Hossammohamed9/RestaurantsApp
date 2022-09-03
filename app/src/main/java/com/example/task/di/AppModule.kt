package com.example.task.di

import com.example.task.util.Constants
import com.example.task.data.remote.ApiService
import com.example.task.data.remote.getunsafeOkHttpClient
import com.example.task.data.repository.Repository
import com.example.task.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApi() : ApiService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .client(getunsafeOkHttpClient().build())
        .build()
        .create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideRepository(api : ApiService) : Repository = Repository(api)

    @Singleton
    @Provides
    fun provideDispatchers() : DispatcherProvider = object : DispatcherProvider{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}
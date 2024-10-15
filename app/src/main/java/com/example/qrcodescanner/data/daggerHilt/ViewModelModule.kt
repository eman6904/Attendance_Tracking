package com.example.qrcodescanner.data.daggerHilt

import com.example.qrcodescanner.data.apis.ApiConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @ViewModelScoped
    @Provides
    fun getConnection(): ApiConnection{
        return ApiConnection()
    }
}
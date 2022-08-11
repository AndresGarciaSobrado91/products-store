package com.garcia.productsstore.common.di

import com.garcia.productsstore.common.Constants
import com.garcia.productsstore.data.remote.client.ProductStoreApi
import com.garcia.productsstore.data.repository.StoreRepositoryImpl
import com.garcia.productsstore.domain.repository.StoreRepository
import com.garcia.productsstore.domain.usecase.CalculateProductFinalPriceUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepository: StoreRepositoryImpl
    ): StoreRepository

    companion object {
        @Provides
        @Singleton
        fun provideCalculateProductFinalPriceUseCase(): CalculateProductFinalPriceUseCase =
            CalculateProductFinalPriceUseCase()


        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun provideProductStoreApi(retrofit: Retrofit): ProductStoreApi =
            retrofit.create(ProductStoreApi::class.java)
    }
}
package com.aman.swipeassignment.di

import com.aman.swipeassignment.data.api.ProductApi
import com.aman.swipeassignment.repository.ProductsRepository
import com.aman.swipeassignment.utils.Constants.BASE_URL
import com.aman.swipeassignment.viewmodels.ProductsViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AppModule {

    private val networkModule = module {

        single {
            OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }

        single {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(get())
                .build()
        }

        single {
            get<Retrofit>().create(ProductApi::class.java)
        }
    }

    private val repositoryModule = module {
        single { ProductsRepository(productApi = get(),androidApplication()) }
    }

    private val viewModelModule = module {
        viewModel { ProductsViewModel(repository = get()) }
    }

    private val appModules: List<Module> = listOf(networkModule, repositoryModule, viewModelModule)


    fun loadModules() {
        loadKoinModules(appModules)
    }
}

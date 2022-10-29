package com.example.csscorechallenge.di

import com.example.csscorechallenge.data.repository.homematches.HomeMatchesRepositoryImpl
import com.example.csscorechallenge.data.service.AppServiceFactory
import com.example.csscorechallenge.domain.repository.HomeMatchesRepository
import com.example.csscorechallenge.domain.usecase.GetHomeMatchesUseCase
import com.example.csscorechallenge.ui.homematches.viewmodel.HomeMatchesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single<HomeMatchesRepository> { HomeMatchesRepositoryImpl(get()) }
    single { GetHomeMatchesUseCase(get()) }

    factory { AppServiceFactory.makeAppService() }

    viewModel {
        HomeMatchesViewModel(
            getHomeMatchesUseCase = get()
        )
    }
}
package com.ludovic.vimont.nasaapod.di

import com.ludovic.vimont.nasaapod.screens.detail.DetailViewModel
import com.ludovic.vimont.nasaapod.screens.home.HomeViewModel
import com.ludovic.vimont.nasaapod.screens.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ViewModelModule {
    val viewModelModule: Module = module {
        viewModel {
            HomeViewModel(get())
        }
        viewModel {
            DetailViewModel(get(), get())
        }
        viewModel {
            SettingsViewModel(get())
        }
    }
}
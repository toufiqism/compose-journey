package com.toufiq.factsninja.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.toufiq.factsninja.data.repository.FactsRepository
import com.toufiq.factsninja.di.DatabaseModule
import com.toufiq.factsninja.di.NetworkModule

class FactsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FactsViewModel::class.java)) {
            val repository = FactsRepository(
                NetworkModule.factsApi,
                DatabaseModule.getDatabase(context).factsDao()
            )
            return FactsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 
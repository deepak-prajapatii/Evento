package com.evento.data.di

import com.evento.data.repositories.EventRepositoryImpl
import com.evento.domain.repositories.EventRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun provideEventRepository(impl: EventRepositoryImpl): EventRepository
}
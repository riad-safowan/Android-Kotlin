package com.riadsafowan.to_do.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.riadsafowan.to_do.data.local.room.task.TaskDao
import com.riadsafowan.to_do.data.local.room.task.TaskDatabase
import com.riadsafowan.to_do.data.local.room.task.TaskRepository
import com.riadsafowan.to_do.data.local.pref.PreferencesRepository
import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.data.remote.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application, callback: TaskDatabase.CallBack) =
        Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    @Singleton
    fun providePreferenceRepository(@ApplicationContext context: Context) =
        PreferencesRepository(context)

    @Provides
    @Singleton
    fun provideUserDataStore(@ApplicationContext context: Context) =
        UserDataStore(context)

    @Provides
    @Singleton
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao) = TaskRepository(taskDao)

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
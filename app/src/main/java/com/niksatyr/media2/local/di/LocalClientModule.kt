package com.niksatyr.media2.local.di

import android.app.Activity
import androidx.activity.ComponentActivity
import com.niksatyr.media2.local.LocalPlayerClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.ref.WeakReference

@Module
@InstallIn(ActivityComponent::class)
internal object LocalClientModule {

    @Provides
    @ActivityScoped
    fun provideLocalPlayerClient(activity: Activity): LocalPlayerClient {
        check(activity is ComponentActivity)
        return LocalPlayerClient(WeakReference(activity))
    }

}
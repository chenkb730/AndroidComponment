package com.android.component.lifecircle

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Seven on 2018/1/5.
 */
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application
}
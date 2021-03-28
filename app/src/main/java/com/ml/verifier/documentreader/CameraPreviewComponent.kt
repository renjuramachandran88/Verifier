package com.ml.verifier.documentreader

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class])
interface CameraPreviewComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CameraPreviewComponent
    }

    fun inject (cameraPreviewActivity: CameraPreviewActivity)
}
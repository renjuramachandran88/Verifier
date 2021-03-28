package com.ml.verifier.documentreader

import android.app.Activity
import android.content.Context

object CameraPreviewComponentManager {
    private lateinit var component: CameraPreviewComponent

    fun createComponent(
        context: Context
    ) {
        component = DaggerCameraPreviewComponent.factory().create(context)
    }

    fun startCameraPreviewActivity(activity: Activity, requestCode: Int) {
        createComponent(activity)
        CameraPreviewActivity.startActivityForResult(activity, requestCode)
    }

    fun getComponent() = component
}
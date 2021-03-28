package com.ml.verifier.documentreader

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class DocumentResult: Parcelable {
    @Parcelize
    data class Success(val capturedString: String): DocumentResult()
    @Parcelize
    object Error: DocumentResult()
}
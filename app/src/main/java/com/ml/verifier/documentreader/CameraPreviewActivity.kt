package com.ml.verifier.documentreader

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.EXTRA_OUTPUT
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider.getUriForFile
import androidx.exifinterface.media.ExifInterface
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.ml.verifier.R
import com.ml.verifier.documentreader.CameraPreviewActivity.Companion.IntentExtra.DOCUMENT_RESULT
import com.ml.verifier.documentreader.DocumentResult.Error
import com.ml.verifier.documentreader.DocumentResult.Success
import java.io.File
import java.io.File.createTempFile
import java.io.IOException

class CameraPreviewActivity : AppCompatActivity() {
    private var photoFile: File? = null
    private var mCurrentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_preview)
        CameraPreviewComponentManager.getComponent().inject(this)
        captureImage()
    }


    private fun captureImage() {
        if (checkSelfPermission(this, CAMERA) != PERMISSION_GRANTED) {
            requestPermissions(
                this,
                arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE),
                CAPTURE_IMAGE_PERMISSION
            )
        } else {
            val takePictureIntent = Intent(ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                try {
                    photoFile = createImageFile()
                    if (photoFile != null) {
                        val photoURI = getUriForFile(
                            this, "com.ml.verifier.provider", photoFile!!
                        )
                        takePictureIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION)

                        takePictureIntent.putExtra(EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                    }
                } catch (ex: Exception) {
                    displayMessage(baseContext, ex.message.toString())
                    setActivityResult(Activity.RESULT_CANCELED, Error)
                }
            } else {
                displayMessage(baseContext, "Null")
                setActivityResult(Activity.RESULT_CANCELED, Error)
            }
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val imageFileName = "JPEG_" + Math.random() + "_"
        val storageDir = getExternalFilesDir(DIRECTORY_PICTURES)
        val image = createTempFile(imageFileName, ".YUV_420_888", storageDir)
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            val bit = rotateImageIfRequired(myBitmap, photoFile!!.absolutePath)
            val image = InputImage.fromBitmap(bit!!, 0)
            val textRecognizer = TextRecognition.getClient()
            textRecognizer.process(image)
                .addOnCompleteListener { visionText ->
                    val resultText = visionText.result.text
                    Log.e("MainActivity", "The text is: $resultText")
                    setActivityResult(RESULT_OK, Success(visionText.result.text))
                }
                .addOnFailureListener { e ->
                    Log.e("MainActivity", e.message.toString())
                    setActivityResult(Activity.RESULT_CANCELED, Error)
                }

        } else if (requestCode == CAPTURE_IMAGE_PERMISSION && resultCode == Activity.RESULT_OK) {
            captureImage()
        } else {
            displayMessage(baseContext, "Request cancelled or something went wrong.")
            setActivityResult(Activity.RESULT_CANCELED, Error)
        }
    }

    @Throws(IOException::class)
    fun rotateImageIfRequired(img: Bitmap, filePath: String): Bitmap? {
        val ei = ExifInterface(filePath)
        val orientation: Int =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    }

    private fun setActivityResult(resultCode: Int, data: DocumentResult) {
        val intent = Intent()
        intent.putExtra(DOCUMENT_RESULT, data)
        setResult(resultCode, intent)
        finish()
    }


    companion object {
        fun startActivityForResult(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, CameraPreviewActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }

        object IntentExtra {
            const val DOCUMENT_RESULT = "document_result"
        }

        const val CAPTURE_IMAGE_REQUEST = 1
        const val CAPTURE_IMAGE_PERMISSION = 0
    }
}













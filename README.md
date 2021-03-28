# Verifier

Verifier is an android SDK to extract texts using ML kit

## Getting Started

SDK requires minimum Android version 5.0 and turn on JAVA 8 compatability on gradle file.

### Installing

Clone the repo and generate aar file by building the project in Android Studio

Add the aar file to your app's libs folder and add aar reference in the project build.gradle file

```
allprojects {
    repositories {
        google()
        jcenter()
        flatDir {
            dirs 'libs'
        }
    }
}
```

and add following dependencies on app gradle file

```
 implementation(name:'verifier', ext:'aar')

 implementation 'com.google.android.gms:play-services-base:17.6.0'
    implementation 'com.google.android.gms:play-services-auth:19.0.0'
    implementation 'com.google.apis:google-api-services-vision:v1-rev16-1.22.0'
    implementation('com.google.api-client:google-api-client-android:1.22.0') {
        exclude module: 'httpclient'
    }
    implementation('com.google.http-client:google-http-client-gson:1.20.0') {
        exclude module: 'httpclient'
    }
```

and add following user permissions in android manifest file
```
<uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />

    <uses-feature
        android:name="android.hardware.camera"
        android:required="true" />
    <uses-feature android:name="android.hardware.camera.autofocus" />
    ```
    
    
    
    ### Implementation
    
    Once the initial set up is done you can use the library by adding 
    ```
     CameraPreviewComponentManager.startCameraPreviewActivity(this, DOCUMENT_READER_REQUEST_CODE)
     ```
     and on providing the necessary permission you will be navigated to camera screen where you need to capture the document.
     
     Upon successfully capturing the texts you will fallback to parent activity by using
     
     ```
      override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DOCUMENT_READER_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.let {
                val documentResult: DocumentResult = data.getParcelableExtra("document_result")!!
                if (documentResult is DocumentResult.Success) {
                    val detectedString = documentResult.capturedString
                    insertVerifierData(detectedString)
                } else {
                    showDisplay("Something went wrong Please try again")
                }
            }
        }
    }
   
    

# Verifier

Verifier is an android SDK to extract texts using ML kit

## Getting Started

SDK requires minimum Android version 5.0 and turn on JAVA 8 compatability on gradle file.

### Installing

clone the repo and generate aar file by building the project in Android Studio

Add the aar file to you app's libs folder and add aar reference in the project build.gradle file

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

and add following dependancies on app gradle file

```
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

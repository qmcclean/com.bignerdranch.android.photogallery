package com.bignerdranch.android.photogallery.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.photogallery.R


class PhotoGalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)

        // This checks to see if the savedInstance state bundle passed into onCreate is null
        // if null, you can safely assume that this is a fresh launch of the activity and no
        // fragments were automatically restored and rehosted
        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction().add(
                    R.id.fragmentContainer,
                    PhotoGalleryFragment.newInstance()
                ).commit()
        }
    }
}

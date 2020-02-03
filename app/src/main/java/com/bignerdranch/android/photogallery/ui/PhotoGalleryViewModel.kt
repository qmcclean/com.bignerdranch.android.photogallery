package com.bignerdranch.android.photogallery.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.photogallery.api.FlickrFetcher
import com.bignerdranch.android.photogallery.model.GalleryItem

/**
 * ViewModel Class is designed to manage UI-related data in a lifecycle conscious way
 * This class will store a live data object which contains a list of gallery items
 * This will kick off a request to fetch the photo data when the viewModel is first
 * initialized and then stashes the resulting live data in my local property
 */
class PhotoGalleryViewModel : ViewModel() {
    val galleryItemLiveData: LiveData<List<GalleryItem>>

    init {
        // this kicks off the service request when this viewModel is called
        // if the user rotates the device, a subsequent call will not be made as the viewModel will remain in memory
        galleryItemLiveData = FlickrFetcher()
            .fetchPhotos()
    }
}
package com.bignerdranch.android.photogallery.ui

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.api.ThumbnailDownloader
import com.bignerdranch.android.photogallery.model.GalleryItem

private const val TAG = "PhotogalleryFragment"

class PhotoGalleryFragment : Fragment() {
    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var thumbnailDownloader: ThumbnailDownloader<PhotoGalleryAdapter.PhotoViewHolder>

    // We will gain access to the ViewModel and stash a reference of the viewModel in a property photoGalleryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This ensurse that the life of the fragment instance matches the users percieved life of the fragment
        retainInstance = true
        photoGalleryViewModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)

        val responseHandler = Handler()
        thumbnailDownloader = ThumbnailDownloader(responseHandler) {
            photoViewHolder, bitmap ->
            val drawable = BitmapDrawable(resources, bitmap)
            photoViewHolder.bindDrawable(drawable)
        }
        lifecycle.addObserver(thumbnailDownloader.fragmentLifecycleObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewLifecycleOwner.lifecycle.addObserver(thumbnailDownloader.viewLifecycleObserver)
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        photoRecyclerView.layoutManager = GridLayoutManager(context, 3)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(thumbnailDownloader.viewLifecycleObserver)
    }

    companion object {
        fun newInstance() =
            PhotoGalleryFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // starting the observation here inside of onViewCreated ensures that the UI Widgets and other related objects are ready.
        // This also handles cases where the fragment becomes detached or is destroyed.
        // In this case, the view will be recreated, the fragment reattached and the liveData subscription will be added to the new view.
        // This can be handled in onCreateView or onActivityCreated as well but is less explicit about the relationship between the
        // live data being observed and the life of the view
        photoGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner,
            Observer {
                galleryItems ->
                photoRecyclerView.adapter =
                    PhotoGalleryAdapter(
                        galleryItems
                    )
            })
    }

    private inner class PhotoGalleryAdapter(private val galleryItems: List<GalleryItem>) : RecyclerView.Adapter<PhotoGalleryAdapter.PhotoViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            return PhotoViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.list_item_gallery,
                        parent,
                        false
                    ) as ImageView
            )
        }

        override fun getItemCount(): Int = galleryItems.size

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            val galleryItem = galleryItems[position]
            thumbnailDownloader.queueThumbnail(holder, galleryItem.url)
        }

        inner class PhotoViewHolder(itemImageView: ImageView) : RecyclerView.ViewHolder(itemImageView) {
            // this is using dataBinding
            val bindDrawable: (Drawable) -> Unit = itemImageView::setImageDrawable
        }
    }
}




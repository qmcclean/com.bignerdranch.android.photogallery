package com.bignerdranch.android.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface FlickrApi {
    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=6220a29171065432b1bfe5240cce7249" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s"

    )
    fun fetchPhotos(): Call<FlickrResponse>

    // This accepts a URL as an input and uses that parameter value directly when determining where to download the data from.
    // Using a parameterless @GET annotation alonf with annotating the first parameter in fetchUrlBytes with @Url causes Retrofit to override the base URL completely.
    // Retrofit will use the URL passed to the fetchUrlBytes function
    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>
}
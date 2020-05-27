package com.android.demo.data.network

import com.android.demo.data.model.BaseResponse
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserApi {

    //https://jsonplaceholder.typicode.com/guide.html

    @GET(Api.posts)
    fun getPosts(): Call<JsonElement>

    @GET(Api.post)
    fun getPostById(@Path("id") id: Int): Call<JsonElement>

    @GET(Api.post_comments)
    fun getPostComments(@Path("id") id: Int): Call<JsonElement>

    @GET(Api.posts)
    fun getPostByUserid(@Query("userId") userId: Int): Call<JsonElement>

    @GET(Api.comments)
    fun getComments(@Query("postId") postId: Int): Call<JsonElement>

    //title: 'foo',
    //      body: 'bar',
    //      userId: 1
    @POST(Api.posts)
    fun postApi(@Body param: HashMap<String, String>): Call<BaseResponse>

//    id: 1,
//    title: 'foo',
//    body: 'bar',
//    userId: 1
    @PUT(Api.post)
    fun putApi(@Path("ID") userId: String, @Body param: HashMap<String, String>): Call<BaseResponse>

    @PATCH(Api.post)
    fun patchApi(@Path("ID") userId: String, @Body param: HashMap<String, String>): Call<BaseResponse>

    @HTTP(method = "DELETE", path = Api.post, hasBody = true)
    fun deleteApi(@Path("ID") userId: String): Call<BaseResponse>

    //
    companion object {
        operator fun invoke(): UserApi {
            return Retrofit.Builder()//
                .client(HttpLoggingInterceptor.build())//
                .baseUrl(Api.baseUrl)//
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
                .addConverterFactory(GsonConverterFactory.create()).build()//
                .create(UserApi::class.java)
        }
    }
}


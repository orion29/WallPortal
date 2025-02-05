package tk.zedlabs.wallaperapp2019.repository

import android.util.Log
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import tk.zedlabs.wallaperapp2019.data.JsonApi
import tk.zedlabs.wallaperapp2019.data.RetrofitService
import tk.zedlabs.wallaperapp2019.models.UnsplashImageDetails
import java.lang.Exception

class PopularDataSource(private val scope: CoroutineScope) : PageKeyedDataSource<Int, UnsplashImageDetails>() {

    val PAGE_SIZE = 20
    val FIRST_PAGE = 1
    val accessKey = "add_api_key_here"
    val orderBy = "popular"
    var  jsonApi : JsonApi

    init {
        jsonApi = RetrofitService.createService(JsonApi::class.java)
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UnsplashImageDetails>) {

        scope.launch {
            try {
                val response = jsonApi.getPopularImages(accessKey,FIRST_PAGE,PAGE_SIZE,orderBy)
                when{
                    response.isSuccessful -> {
                        callback.onResult(response.body()!!,null,FIRST_PAGE+1)
                    }
                }
            }catch (exception : Exception){
                Log.e("repository->Posts",""+exception.message)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashImageDetails>) {

        scope.launch {
            try {
                val response = jsonApi.getPopularImages(accessKey,params.key,PAGE_SIZE,orderBy)
                when{
                    response.isSuccessful -> {
                         val key: Int?
                         if(response.body()?.isNotEmpty()!!) key = params.key+1
                         else key = null
                         callback.onResult(response.body()!!,key)
                    }
                }
            }catch (exception : Exception){
                Log.e("repository->Popular",""+exception.message)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashImageDetails>) {

        scope.launch {
            try {
                val response = jsonApi.getPopularImages(accessKey,params.key,PAGE_SIZE,orderBy)
                val key: Int?
                if(params.key > 1) key = params.key-1
                else key = null
                when{
                    response.isSuccessful -> {
                        callback.onResult(response.body()!!,key)
                    }
                }
            }catch (exception : Exception){
                Log.e("repository->Popular",""+exception.message)
            }
        }
    }
    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }
}

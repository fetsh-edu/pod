package me.fetsh.geekbrains.pod.ui.giphy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GiphyViewModel(
    private val liveDataForViewToObserve: MutableLiveData<GiphyData> = MutableLiveData(GiphyData.NotAsked),
    private val retrofitImpl: GiphyRetrofit = GiphyRetrofit()
) :
    ViewModel() {

    val liveData : LiveData<GiphyData>
        get() { return liveDataForViewToObserve }

    fun sendServerRequest(tag: String = "party hard") {
        liveDataForViewToObserve.value = GiphyData.Loading(null)

        retrofitImpl.getRetrofitImpl().getRandomGif(
            apiKey = GIPHY_API_KEY,
            tag = tag
        ).enqueue(object :
            Callback<GiphyResponseData> {
            override fun onResponse(
                call: Call<GiphyResponseData>,
                response: Response<GiphyResponseData>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    liveDataForViewToObserve.value =
                        GiphyData.Success(response.body()!!)
                } else {
                    val message = response.message()
                    if (message.isNullOrEmpty()) {
                        liveDataForViewToObserve.value =
                            GiphyData.Error(Throwable("Unidentified error"))
                    } else {
                        liveDataForViewToObserve.value =
                            GiphyData.Error(Throwable(message))
                    }
                }
            }

            override fun onFailure(call: Call<GiphyResponseData>, t: Throwable) {
                liveDataForViewToObserve.value = GiphyData.Error(t)
            }
        })
    }
}

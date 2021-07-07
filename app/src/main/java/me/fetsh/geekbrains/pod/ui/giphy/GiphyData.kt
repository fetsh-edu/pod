package me.fetsh.geekbrains.pod.ui.giphy

sealed class GiphyData {
    data class Success(val serverResponseData: GiphyResponseData) : GiphyData()
    data class Error(val error: Throwable) : GiphyData()
    data class Loading(val progress: Int?) : GiphyData()
}
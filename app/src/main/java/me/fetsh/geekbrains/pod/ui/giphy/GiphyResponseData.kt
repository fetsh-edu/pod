package me.fetsh.geekbrains.pod.ui.giphy

import com.google.gson.annotations.SerializedName

data class GiphyResponseData(
    @field:SerializedName("data") val data: GifData?,
) {
    data class GifData(
        @field:SerializedName("type") val type: String?,
        @field:SerializedName("id") val id: String?,
        @field:SerializedName("url") val url: String?,
        @field:SerializedName("embed_url") val embed_url: String?,
        @field:SerializedName("title") val title: String?,
    )

    val gif: String?
        get() {
            return data?.id?.let {
                "https://media1.giphy.com/media/$it/200w.gif"
            }
        }
}

package com.example.f1info.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder

object WikipediaImageFetcher {
    fun extractTitleFromUrl(url: String): String? {
        val marker = "/wiki/"
        val index = url.indexOf(marker)
        if (index == -1) return null
        val title = url.substring(index + marker.length).substringBefore("#")
        return URLDecoder.decode(title, "UTF-8")
    }

    suspend fun fetchThumbnailUrl(title: String, sizePx: Int = 400): String? {
        return withContext(Dispatchers.IO) {
            val encodedTitle = URLEncoder.encode(title, "UTF-8")
            val apiUrl =
                "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&titles=$encodedTitle&format=json&pithumbsize=$sizePx&pilicense=any"
            val response = URL(apiUrl).readText()
            val pages = JSONObject(response)
                .getJSONObject("query")
                .getJSONObject("pages")
            val keys = pages.keys()
            if (keys.hasNext()) {
                val page = pages.getJSONObject(keys.next())
                page.optJSONObject("thumbnail")?.optString("source")
            } else {
                null
            }
        }
    }
}

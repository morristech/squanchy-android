package net.squanchy.tweets.service

import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Search
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Call

class TwitterRepository {

    private val searchService = TwitterCore.getInstance().apiClient.searchService

    internal fun load(query: String): Single<Search> {
        return Single.fromCallable { createSearchRequest(query).execute().body() }
            .subscribeOn(Schedulers.io())
    }

    private fun createSearchRequest(query: String): Call<Search> =
        searchService.tweets(query, null, null, null, "recent", MAX_ITEM_PER_REQUEST, null, null, null, true)

    companion object {

        private const val MAX_ITEM_PER_REQUEST = 100
    }
}

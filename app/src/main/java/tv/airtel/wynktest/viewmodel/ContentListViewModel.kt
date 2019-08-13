package tv.airtel.wynktest.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dfl.newsapi.NewsApiRepository
import com.dfl.newsapi.model.ArticleDto
import com.dfl.newsapi.model.ArticlesDto
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tv.airtel.wynktest.data.FavouriteClick
import tv.airtel.wynktest.data.apiKey
import tv.airtel.wynktest.data.persistance.AppDatabase
import tv.airtel.wynktest.data.persistance.FavouriteModel
import tv.airtel.wynktest.model.ContentParams

/**
 * @author Vipul Kumar; dated 22/10/18.
 *
 * If the api throws HTTP 429, please find the [apiKey]
 * and uncomment a different key.
 */
class ContentListViewModel : BaseViewModel() {
    internal val result = MutableLiveData<Result<ArticlesDto>>()
    internal val addedToFavourite = MutableLiveData<Result<Int>>()
    internal val favouriteElement = MutableLiveData<Result<List<FavouriteModel>>>()
    internal val favChange = MutableLiveData<FavouriteClick>()


    /**
     * Fetches news from sources.
     * You do not need to understand this implementation in order to complete the assignment.
     * All the updates (response) are posted to the [result] liveData.
     */
    fun fetchNews(params: ContentParams = ContentParams()) {
        NewsApiRepository(apiKey = apiKey).getEverything(
                q = params.q,
                sources = params.sources,
                domains = params.domains,
                language = params.language,
                sortBy = params.sortBy,
                pageSize = params.pageSize,
                page = params.page
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable()
                .subscribe(
                        { result.value = Result.success(it) },
                        {
                            Log.e("Error", it.cause?.message ?: "")
                            result.value = Result.failure(it)
                        }
                ).disposeOnClear()
    }


    /**
     * Fetches news from sources.
     * You do not need to understand this implementation in order to complete the assignment.
     * All the updates (response) are posted to the [result] liveData.
     */
    fun fetchFavourite() {
        AppDatabase.instance.favouriteDat().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable()
                .subscribe(
                        { favouriteElement.value = Result.success(it) },
                        {
                            Log.e("Error", it.cause?.message ?: "")
                            result.value = Result.failure(it)
                        }
                ).disposeOnClear()
    }

    fun saveToDb(favouriteClick: FavouriteClick) {
        Single.fromCallable {
            if (favouriteClick.remove) {
                AppDatabase.instance.favouriteDat().delete(favouriteClick.primaryKey)
            } else {
                AppDatabase.instance.favouriteDat().insert(FavouriteModel(favouriteClick.primaryKey))
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ addedToFavourite.value = Result.success(favouriteClick.postion) },
                        {
                            Log.e("Error", it.cause?.message ?: "")
                            addedToFavourite.value = Result.failure(it)
                        }).disposeOnClear()
    }

    fun sortBySource(articleElement: List<ArticleDto>): MutableList<ArticleDto> {
        val pair = linkedMapOf<String, MutableList<ArticleDto>>()
        articleElement.forEach {
            if (!pair.containsKey(it.source.name)) {
                pair[it.source.name] = mutableListOf()
            }
            pair[it.source.name]?.add(it)
        }

        val items = mutableListOf<ArticleDto>()
        pair.values.forEach {
            items.addAll(it)
        }

        return items
    }
}


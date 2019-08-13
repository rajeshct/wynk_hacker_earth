package tv.airtel.wynktest

import com.dfl.newsapi.model.ArticleDto
import tv.airtel.wynktest.data.persistance.FavouriteModel

object Utils {
    fun getFavouritePrimaryKey(articleDto: ArticleDto) =
            articleDto.author + articleDto.publishedAt + articleDto.source

    fun containFav(listOfFavouriteNews: MutableList<FavouriteModel>, primaryKey: String): Boolean {
        listOfFavouriteNews.forEach {
            if (it.primaryKey.equals(primaryKey, true)) {
                return true
            }
        }
        return false
    }

}
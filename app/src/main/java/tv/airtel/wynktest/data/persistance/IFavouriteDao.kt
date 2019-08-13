package tv.airtel.wynktest.data.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface IFavouriteDao {
    @Query("SELECT * FROM favouritemodel ")
    fun getAll(): Single<List<FavouriteModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: FavouriteModel)

    @Query("delete from favouritemodel  WHERE primaryKey = :content")
    fun delete(content: String)

}
package tv.airtel.wynktest.data.persistance

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteModel(
        @ColumnInfo(name = "primaryKey")
        @PrimaryKey
        var primaryKey: String
)


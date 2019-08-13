package tv.airtel.wynktest.data.persistance

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tv.airtel.wynktest.CustomApplication

@Database(entities = [(FavouriteModel::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteDat(): IFavouriteDao

    private object Holder {
        val INSTANCE = synchronized(AppDatabase::class.java) {
            Room.databaseBuilder(CustomApplication.instance.applicationContext,
                AppDatabase::class.java, "WynkTest")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    companion object {
        val instance: AppDatabase by lazy { Holder.INSTANCE }
    }

}
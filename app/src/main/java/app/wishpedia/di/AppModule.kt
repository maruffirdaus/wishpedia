package app.wishpedia.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import app.wishpedia.data.AppRepository
import app.wishpedia.data.source.AppDatabase
import app.wishpedia.data.source.dao.CategoryDao
import app.wishpedia.data.source.dao.ItemDao
import app.wishpedia.data.source.dao.TagDao
import app.wishpedia.utils.InitialDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRepository(
        @ApplicationContext
        context: Context,
        categoryDao: CategoryDao,
        itemDao: ItemDao,
        tagDao: TagDao
    ): AppRepository {
        return AppRepository(context, categoryDao, itemDao, tagDao)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context, scope: CoroutineScope): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "Wishpedia.db"
        ).addCallback(object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                scope.launch {
                    val category = InitialDataSource.category
                    val tags = InitialDataSource.tags
                    db.execSQL("INSERT INTO category (id, name) VALUES (${category.id}, \"${category.name}\")")
                    tags.forEach {
                        db.execSQL("INSERT INTO tag (id, name, point) VALUES (${it.id}, \"${it.name}\", ${it.point})")
                    }
                }
            }
        }).build()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase) = database.categoryDao()

    @Provides
    @Singleton
    fun provideItemDao(database: AppDatabase) = database.itemDao()

    @Provides
    @Singleton
    fun provideTagDao(database: AppDatabase) = database.tagDao()

    @Provides
    @Singleton
    fun provideCoroutineScope() = CoroutineScope(SupervisorJob())
}
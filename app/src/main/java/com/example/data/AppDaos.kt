package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Query("SELECT * FROM shops ORDER BY rating DESC, id DESC")
    fun getAllShops(): Flow<List<ShopEntity>>

    @Query("SELECT * FROM shops WHERE id = :id")
    fun getShopById(id: Int): Flow<ShopEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShop(shop: ShopEntity): Long

    @Update
    suspend fun updateShop(shop: ShopEntity)

    @Delete
    suspend fun deleteShop(shop: ShopEntity)
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE shopId = :shopId ORDER BY id DESC")
    fun getProductsByShop(shopId: Int): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)
}

@Dao
interface NewsDao {
    @Query("SELECT * FROM news ORDER BY timestamp DESC")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsEntity)

    @Delete
    suspend fun deleteNews(news: NewsEntity)
}

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs ORDER BY id DESC")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity)

    @Delete
    suspend fun deleteJob(job: JobEntity)
}

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY id DESC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE shopId = :shopId ORDER BY timestamp DESC")
    fun getReviewsForShop(shopId: Int): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews ORDER BY timestamp DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Delete
    suspend fun deleteReview(review: ReviewEntity)
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE userId = :userId")
    fun getFavoritesOfUser(userId: String): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE userId = :userId AND shopId = :shopId LIMIT 1")
    fun getFavorite(userId: String, shopId: Int): Flow<FavoriteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)
}

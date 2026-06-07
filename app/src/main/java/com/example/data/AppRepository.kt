package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val database: AppDatabase) {
    
    // Shop operations
    val allShops: Flow<List<ShopEntity>> = database.shopDao().getAllShops()
    
    fun getShopById(id: Int): Flow<ShopEntity?> {
        return database.shopDao().getShopById(id)
    }
    
    suspend fun insertShop(shop: ShopEntity): Long {
        return database.shopDao().insertShop(shop)
    }
    
    suspend fun updateShop(shop: ShopEntity) {
        database.shopDao().updateShop(shop)
    }
    
    suspend fun deleteShop(shop: ShopEntity) {
        database.shopDao().deleteShop(shop)
    }

    // Product operations
    fun getProductsByShop(shopId: Int): Flow<List<ProductEntity>> {
        return database.productDao().getProductsByShop(shopId)
    }
    
    suspend fun insertProduct(product: ProductEntity) {
        database.productDao().insertProduct(product)
    }
    
    suspend fun updateProduct(product: ProductEntity) {
        database.productDao().updateProduct(product)
    }
    
    suspend fun deleteProduct(product: ProductEntity) {
        database.productDao().deleteProduct(product)
    }

    // News operations
    val allNews: Flow<List<NewsEntity>> = database.newsDao().getAllNews()
    
    suspend fun insertNews(news: NewsEntity) {
        database.newsDao().insertNews(news)
    }
    
    suspend fun deleteNews(news: NewsEntity) {
        database.newsDao().deleteNews(news)
    }

    // Job operations
    val allJobs: Flow<List<JobEntity>> = database.jobDao().getAllJobs()
    
    suspend fun insertJob(job: JobEntity) {
        database.jobDao().insertJob(job)
    }
    
    suspend fun deleteJob(job: JobEntity) {
        database.jobDao().deleteJob(job)
    }

    // Event operations
    val allEvents: Flow<List<EventEntity>> = database.eventDao().getAllEvents()
    
    suspend fun insertEvent(event: EventEntity) {
        database.eventDao().insertEvent(event)
    }
    
    suspend fun deleteEvent(event: EventEntity) {
        database.eventDao().deleteEvent(event)
    }

    // Review operations
    val allReviewsForAdmin: Flow<List<ReviewEntity>> = database.reviewDao().getAllReviews()

    fun getReviewsForShop(shopId: Int): Flow<List<ReviewEntity>> {
        return database.reviewDao().getReviewsForShop(shopId)
    }
    
    suspend fun insertReview(review: ReviewEntity) {
        database.reviewDao().insertReview(review)
    }
    
    suspend fun deleteReview(review: ReviewEntity) {
        database.reviewDao().deleteReview(review)
    }

    // Favorite operations
    fun getFavoritesOfUser(userId: String): Flow<List<FavoriteEntity>> {
        return database.favoriteDao().getFavoritesOfUser(userId)
    }
    
    fun getFavorite(userId: String, shopId: Int): Flow<FavoriteEntity?> {
        return database.favoriteDao().getFavorite(userId, shopId)
    }
    
    suspend fun insertFavorite(favorite: FavoriteEntity) {
        database.favoriteDao().insertFavorite(favorite)
    }
    
    suspend fun deleteFavorite(favorite: FavoriteEntity) {
        database.favoriteDao().deleteFavorite(favorite)
    }
}

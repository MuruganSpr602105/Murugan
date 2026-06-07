package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shops")
data class ShopEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val tamilName: String,
    val category: String, // e.g. "Restaurant", "Medical", "Supermarket", "Industry", "Education", "Hospital", "Other"
    val address: String,
    val contactNo: String,
    val whatsappNo: String,
    val mapLocationUrl: String,
    val openingTime: String, // e.g. "09:00 AM"
    val closingTime: String, // e.g. "09:00 PM"
    val imageUrl: String,
    val productsText: String, // Comma-separated or lines of products
    val offers: String,
    val hasHomeDelivery: Boolean,
    val isApprovedContent: Boolean = true, // Default to true for pre-populated, false for user registrations till admin approves
    val rating: Float = 4.0f,
    val ratingCount: Int = 1,
    val area: String, // "SIPCOT", "Bus Stand", "Sunguvarchatram", "Theradi", "Oragadam", "Pennalur"
    val isOwnerRegistered: Boolean = false
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val shopId: Int,
    val name: String,
    val tamilName: String,
    val price: Double,
    val inStock: Boolean = true,
    val imageUrl: String = ""
)

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val tamilTitle: String,
    val content: String,
    val tamilContent: String,
    val category: String, // "Traffic", "Announcement", "Government", "General"
    val timestamp: Long = System.currentTimeMillis(),
    val author: String = "Sriperumbudur Connect Admin"
)

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val companyName: String,
    val title: String,
    val tamilTitle: String,
    val description: String,
    val tamilDescription: String,
    val jobType: String, // "Factory", "Full-Time", "Part-Time"
    val location: String, // "SIPCOT", "Irungattukottai", "Oragadam", "Town Center"
    val salaryRange: String,
    val contactPhone: String,
    val email: String
)

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val tamilTitle: String,
    val description: String,
    val tamilDescription: String,
    val date: String,
    val time: String,
    val venue: String, // e.g. "Adikesava Perumal Temple"
    val organizer: String
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val shopId: Int,
    val userName: String,
    val rating: Int, // 1 to 5
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorites", primaryKeys = ["userId", "shopId"])
data class FavoriteEntity(
    val userId: String,
    val shopId: Int
)

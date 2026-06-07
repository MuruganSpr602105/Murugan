package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(private val repository: AppRepository) : ViewModel() {

    // User settings and role states
    private val _isTamil = MutableStateFlow(false)
    val isTamil: StateFlow<Boolean> = _isTamil.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Active filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedArea = MutableStateFlow("All")
    val selectedArea: StateFlow<String> = _selectedArea.asStateFlow()

    // Current User profile state
    private val _userName = MutableStateFlow("Murugan K")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userPhone = MutableStateFlow("9840123456")
    val userPhone: StateFlow<String> = _userPhone.asStateFlow()

    private val _userRole = MutableStateFlow("User") // "User", "Shop Owner", "Admin"
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    // Favorites cache for instant UI lookups
    val userFavorites: StateFlow<List<FavoriteEntity>> = repository.getFavoritesOfUser("default_user")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All elements mapped directly from Room Repository Flows
    val allShops: StateFlow<List<ShopEntity>> = repository.allShops
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNews: StateFlow<List<NewsEntity>> = repository.allNews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allJobs: StateFlow<List<JobEntity>> = repository.allJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEvents: StateFlow<List<EventEntity>> = repository.allEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReviewsForAdmin: StateFlow<List<ReviewEntity>> = repository.allReviewsForAdmin
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered shops based on search query, category, area, and approval status.
    // Standard users only see APPROVED shops, while Shop Owners and Admins can see all.
    val filteredShops: StateFlow<List<ShopEntity>> = combine(
        allShops,
        _searchQuery,
        _selectedCategory,
        _selectedArea,
        _userRole
    ) { shops, query, category, area, role ->
        shops.filter { shop ->
            // Visibility logic
            val isVisible = shop.isApprovedContent || role == "Admin" || (role == "Shop Owner" && shop.contactNo == _userPhone.value)
            
            val matchesCategory = category == "All" || shop.category == category
            val matchesArea = area == "All" || shop.area == area
            
            val matchesSearch = query.isEmpty() ||
                    shop.name.contains(query, ignoreCase = true) ||
                    shop.tamilName.contains(query, ignoreCase = true) ||
                    shop.productsText.contains(query, ignoreCase = true) ||
                    shop.category.contains(query, ignoreCase = true) ||
                    shop.area.contains(query, ignoreCase = true)

            isVisible && matchesCategory && matchesArea && matchesSearch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Language & Mode functions
    fun toggleLanguage() {
        _isTamil.value = !_isTamil.value
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    // Filter mutations
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSelectedArea(area: String) {
        _selectedArea.value = area
    }

    // Profile settings
    fun updateProfile(name: String, phone: String) {
        _userName.value = name
        _userPhone.value = phone
    }

    fun setUserRole(role: String) {
        _userRole.value = role
    }

    fun getShopById(id: Int): Flow<ShopEntity?> {
        return repository.getShopById(id)
    }

    // CRUD - Shop operations
    fun registerShop(
        name: String,
        tamilName: String,
        category: String,
        address: String,
        contactNo: String,
        whatsappNo: String,
        mapLocationUrl: String,
        openingTime: String,
        closingTime: String,
        imageUrl: String,
        productsText: String,
        offers: String,
        hasHomeDelivery: Boolean,
        area: String,
        isOwner: Boolean = false
    ) {
        viewModelScope.launch {
            // Self-registered shops remain pending approval (isApprovedContent = false) unless signed as Admin
            val isApproved = (_userRole.value == "Admin")
            val newShop = ShopEntity(
                name = name,
                tamilName = tamilName,
                category = category,
                address = address,
                contactNo = contactNo,
                whatsappNo = whatsappNo,
                mapLocationUrl = mapLocationUrl,
                openingTime = openingTime,
                closingTime = closingTime,
                imageUrl = imageUrl,
                productsText = productsText,
                offers = offers,
                hasHomeDelivery = hasHomeDelivery,
                area = area,
                isApprovedContent = isApproved,
                isOwnerRegistered = isOwner
            )
            repository.insertShop(newShop)
        }
    }

    fun updateShopDetails(shop: ShopEntity) {
        viewModelScope.launch {
            repository.updateShop(shop)
        }
    }

    fun approveShop(shop: ShopEntity) {
        viewModelScope.launch {
            repository.updateShop(shop.copy(isApprovedContent = true))
        }
    }

    fun deleteShop(shop: ShopEntity) {
        viewModelScope.launch {
            repository.deleteShop(shop)
        }
    }

    // CRUD - Product operations
    fun getProductsForShop(shopId: Int): Flow<List<ProductEntity>> {
        return repository.getProductsByShop(shopId)
    }

    fun addProduct(shopId: Int, name: String, tamilName: String, price: Double, inStock: Boolean) {
        viewModelScope.launch {
            repository.insertProduct(
                ProductEntity(
                    shopId = shopId,
                    name = name,
                    tamilName = tamilName,
                    price = price,
                    inStock = inStock
                )
            )
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    // CRUD - Post operations (News, Jobs, Events)
    fun addLocalNews(title: String, tamilTitle: String, content: String, tamilContent: String, category: String) {
        viewModelScope.launch {
            repository.insertNews(
                NewsEntity(
                    title = title,
                    tamilTitle = tamilTitle,
                    content = content,
                    tamilContent = tamilContent,
                    category = category
                )
            )
        }
    }

    fun deleteNews(news: NewsEntity) {
        viewModelScope.launch {
            repository.deleteNews(news)
        }
    }

    fun addJobVacancy(
        company: String,
        title: String,
        tamilTitle: String,
        desc: String,
        tamilDesc: String,
        type: String,
        location: String,
        salary: String,
        phone: String,
        email: String
    ) {
        viewModelScope.launch {
            repository.insertJob(
                JobEntity(
                    companyName = company,
                    title = title,
                    tamilTitle = tamilTitle,
                    description = desc,
                    tamilDescription = tamilDesc,
                    jobType = type,
                    location = location,
                    salaryRange = salary,
                    contactPhone = phone,
                    email = email
                )
            )
        }
    }

    fun deleteJob(job: JobEntity) {
        viewModelScope.launch {
            repository.deleteJob(job)
        }
    }

    fun addTownEvent(
        title: String,
        tamilTitle: String,
        desc: String,
        tamilDesc: String,
        date: String,
        time: String,
        venue: String,
        organizer: String
    ) {
        viewModelScope.launch {
            repository.insertEvent(
                EventEntity(
                    title = title,
                    tamilTitle = tamilTitle,
                    description = desc,
                    tamilDescription = tamilDesc,
                    date = date,
                    time = time,
                    venue = venue,
                    organizer = organizer
                )
            )
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.deleteEvent(event)
        }
    }

    // CRUD - Reviews
    fun getReviewsForShop(shopId: Int): Flow<List<ReviewEntity>> {
        return repository.getReviewsForShop(shopId)
    }

    fun addReview(shopId: Int, rating: Int, comment: String) {
        viewModelScope.launch {
            repository.insertReview(
                ReviewEntity(
                    shopId = shopId,
                    userName = _userName.value,
                    rating = rating,
                    comment = comment
                )
            )
        }
    }

    fun deleteReview(review: ReviewEntity) {
        viewModelScope.launch {
            repository.deleteReview(review)
        }
    }

    // Bookmark Toggle Logic
    fun toggleFavorite(shopId: Int) {
        viewModelScope.launch {
            val exists = userFavorites.value.any { it.shopId == shopId }
            if (exists) {
                repository.deleteFavorite(FavoriteEntity("default_user", shopId))
            } else {
                repository.insertFavorite(FavoriteEntity("default_user", shopId))
            }
        }
    }
}

class AppViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

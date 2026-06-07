package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductEntity
import com.example.data.ReviewEntity
import com.example.data.ShopEntity
import com.example.ui.AppViewModel
import com.example.ui.localization.LocaleManager
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailScreen(
    shopId: Int,
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val isTamil by viewModel.isTamil.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val userFavorites by viewModel.userFavorites.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()

    val shopFlow = remember(shopId) { viewModel.getShopById(shopId) }
    val shop by shopFlow.collectAsState(initial = null)

    val productsFlow = remember(shopId) { viewModel.getProductsForShop(shopId) }
    val productsList by productsFlow.collectAsState(initial = emptyList())

    val reviewsFlow = remember(shopId) { viewModel.getReviewsForShop(shopId) }
    val reviewsList by reviewsFlow.collectAsState(initial = emptyList())

    val context = LocalContext.current

    // Modal state for Shop Owner adding a product
    var showAddProductDialog by remember { mutableStateOf(false) }

    // Modal state for submitting a review
    var showAddReviewDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = shop?.let { LocaleManager.t(it.name, it.tamilName, isTamil) } ?: "Shop Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    shop?.let { currentShop ->
                        // Favorites bookmark shortcut
                        val isFav = userFavorites.any { it.shopId == currentShop.id }
                        IconButton(onClick = { viewModel.toggleFavorite(currentShop.id) }) {
                            Icon(
                                imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Toggle Favorite",
                                tint = if (isFav) Color.Red else MaterialTheme.colorScheme.primary
                            )
                        }

                        // Share Profile details (Requirement 8)
                        IconButton(onClick = {
                            val shareBody = """
                                *${currentShop.name} (${currentShop.category})*
                                📍 Location: ${currentShop.area}, Sriperumbudur
                                🏢 Address: ${currentShop.address}
                                📞 Contact: ${currentShop.contactNo}
                                💬 WhatsApp: https://wa.me/91${currentShop.whatsappNo}
                                Get more info on Sriperumbudur Connect app!
                            """.trimIndent()
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, currentShop.name)
                                putExtra(Intent.EXTRA_TEXT, shareBody)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Shop Profile"))
                        }) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share shop details")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        shop?.let { currentShop ->
            // Check if current user owns this shop (based on registered contact matches profile contact) OR is Admin
            val canManageProducts = userRole == "Admin" || 
                    (userRole == "Shop Owner" && currentShop.contactNo == userPhone)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag("shop_detail_scroll_view"),
                contentPadding = PaddingValues(16.dp)
            ) {
                // 1. Interactive Header Cards
                item {
                    DetailHeaderPane(currentShop, isTamil)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 2. Local offers billboard
                if (currentShop.offers.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Percent, contentDescription = "Offer", tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = LocaleManager.t("SPECIAL OFFER FOR YOU:", "உங்களுக்கான சிறப்பு சலுகை:", isTamil),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = currentShop.offers,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // 3. Communications Panel (Call, WhatsApp, Maps)
                item {
                    CommunicationShortcutsPanel(currentShop, isTamil)
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // 4. Products list heading
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocaleManager.t("Product Catalog / Menu", "தயாரிப்புகள் மற்றும் மெனு", isTamil),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (canManageProducts) {
                            TextButton(onClick = { showAddProductDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Product Icon", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(LocaleManager.t("Add Product", "தயாரிப்புச் சேர்", isTamil), fontSize = 13.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // 5. Products catalog lists
                if (productsList.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.ProductionQuantityLimits, contentDescription = "Empty", tint = MaterialTheme.colorScheme.outline)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    LocaleManager.t("No products listed yet.", "தயாரிப்புகள் ஏதும் வெளியிடப்படவில்லை.", isTamil),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                } else {
                    items(productsList) { product ->
                        ProductItemRow(product, canManageProducts, isTamil, onDeleteProduct = { viewModel.deleteProduct(product) })
                    }
                    item { Spacer(modifier = Modifier.height(12.dp)) }
                }

                // 6. Customer Reviews Board
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocaleManager.t(LocaleManager.reviewsTitleEnTa.first, LocaleManager.reviewsTitleEnTa.second, isTamil),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { showAddReviewDialog = true }) {
                            Icon(Icons.Default.RateReview, contentDescription = "Review Icon", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(LocaleManager.t("Write Review", "மதிப்பீடு எழுது", isTamil), fontSize = 13.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (reviewsList.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    LocaleManager.t("Be the first to review this business!", "இந்தக் கடைக்கு முதல் மதிப்புரையை வழங்கவும்!", isTamil),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                } else {
                    items(reviewsList) { review ->
                        ReviewItemRow(
                            review = review,
                            isAdmin = userRole == "Admin",
                            onDelete = { viewModel.deleteReview(review) }
                        )
                    }
                }
            }
        } ?: run {
            // Loading skeleton
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    // Add Product Dialog (Shop Owner only)
    if (showAddProductDialog) {
        var prodNameEn by remember { mutableStateOf("") }
        var prodNameTa by remember { mutableStateOf("") }
        var prodPrice by remember { mutableStateOf("") }
        var inStock by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { showAddProductDialog = false },
            title = { Text(LocaleManager.t("Add New Product", "புதிய தயாரிப்பினைச் சேர்த்தல்", isTamil)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = prodNameEn,
                        onValueChange = { prodNameEn = it },
                        label = { Text(LocaleManager.t("Product Name (English)", "தயாரிப்பு பெயர் (ஆங்கிலம்)", isTamil)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = prodNameTa,
                        onValueChange = { prodNameTa = it },
                        label = { Text(LocaleManager.t("Product Name (Tamil)", "தயாரிப்பு பெயர் (தமிழ்)", isTamil)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = prodPrice,
                        onValueChange = { prodPrice = it },
                        label = { Text(LocaleManager.t("Price (₹)", "விலை (₹)", isTamil)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(LocaleManager.t("Stock Availability", "இருப்பு விவரம்", isTamil))
                        Switch(checked = inStock, onCheckedChange = { inStock = it })
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedPrice = prodPrice.toDoubleOrNull() ?: 0.0
                        if (prodNameEn.isNotEmpty()) {
                            viewModel.addProduct(shopId, prodNameEn, prodNameTa, parsedPrice, inStock)
                            showAddProductDialog = false
                        }
                    }
                ) {
                    Text(LocaleManager.t("Add", "சேர்", isTamil))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddProductDialog = false }) {
                    Text(LocaleManager.t("Cancel", "ரத்துசெய்", isTamil))
                }
            }
        )
    }

    // Add Review Dialog (Everyone)
    if (showAddReviewDialog) {
        var ratingVal by remember { mutableStateOf(5) }
        var commentEnTa by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddReviewDialog = false },
            title = { Text(LocaleManager.t("Submit Your Feedback", "உங்கள் மதிப்புரையை வழங்குக", isTamil)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(LocaleManager.t("Select Rating Stars", "மதிப்பீட்டு நட்சத்திரங்களை தேர்வு செய்க", isTamil), fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        (1..5).forEach { star ->
                            IconButton(onClick = { ratingVal = star }) {
                                Icon(
                                    imageVector = if (star <= ratingVal) Icons.Filled.Star else Icons.Filled.StarBorder,
                                    contentDescription = "Star $star",
                                    tint = if (star <= ratingVal) Color(0xFFFBC02D) else MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = commentEnTa,
                        onValueChange = { commentEnTa = it },
                        placeholder = { Text(LocaleManager.t("Write details here...", "உங்கள் கருத்துக்களை இங்கே பதிவிடவும்...", isTamil)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (commentEnTa.isNotEmpty()) {
                            viewModel.addReview(shopId, ratingVal, commentEnTa)
                            showAddReviewDialog = false
                        }
                    }
                ) {
                    Text(LocaleManager.t("Submit", "சமர்ப்பி", isTamil))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddReviewDialog = false }) {
                    Text(LocaleManager.t("Cancel", "ரத்துசெய்", isTamil))
                }
            }
        )
    }
}

@Composable
fun DetailHeaderPane(shop: ShopEntity, isTamil: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SuggestionChip(
                    onClick = {},
                    label = { Text(shop.category.uppercase(), fontWeight = FontWeight.Bold) }
                )
                Text(
                    text = "⏰ ${shop.openingTime} - ${shop.closingTime}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = LocaleManager.t(shop.name, shop.tamilName, isTamil),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "📍 ${shop.address} (${shop.area})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFBC02D),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format("%.1f", shop.rating),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "(${shop.ratingCount} ${LocaleManager.t("reviews", "மதிப்பீடுகள்", isTamil)})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun CommunicationShortcutsPanel(shop: ShopEntity, isTamil: Boolean) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Call Primary Number
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${shop.contactNo}"))
                        context.startActivity(intent)
                    }
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Call", tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(LocaleManager.t("Call Shop", "தொடர்பு கொள்க", isTamil), style = MaterialTheme.typography.labelSmall)
            }

            // Msg WhatsApp
            if (shop.whatsappNo.isNotEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            val url = "https://api.whatsapp.com/send?phone=91${shop.whatsappNo}"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Message, contentDescription = "WhatsApp Msg", tint = Color(0xFF4CAF50))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("WhatsApp", style = MaterialTheme.typography.labelSmall)
                }
            }

            // Google Maps direction link
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(shop.mapLocationUrl))
                        context.startActivity(intent)
                    }
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Map, contentDescription = "Maps", tint = MaterialTheme.colorScheme.secondary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(LocaleManager.t("Directions", "வழிபடம்", isTamil), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun ProductItemRow(
    product: ProductEntity,
    canManage: Boolean,
    isTamil: Boolean,
    onDeleteProduct: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = LocaleManager.t(product.name, product.tamilName.ifEmpty { product.name }, isTamil),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "₹${product.price}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (product.inStock) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE8F5E9), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = LocaleManager.t("In Stock", "இருப்பில் உள்ளது", isTamil),
                            color = Color(0xFF2E7D32),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFEBEE), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = LocaleManager.t("Out of Stock", "இருப்பில் இல்லை", isTamil),
                            color = Color(0xFFC62828),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (canManage) {
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { onDeleteProduct() }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Product", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewItemRow(
    review: ReviewEntity,
    isAdmin: Boolean,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.userName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    (1..5).forEach { star ->
                        Icon(
                            imageVector = if (star <= review.rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = "star",
                            tint = if (star <= review.rating) Color(0xFFFBC02D) else Color.LightGray,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    
                    if (isAdmin) {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Moderate", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

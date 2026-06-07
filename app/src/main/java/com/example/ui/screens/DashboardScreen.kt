package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EventEntity
import com.example.data.JobEntity
import com.example.data.NewsEntity
import com.example.data.ShopEntity
import com.example.ui.AppViewModel
import com.example.ui.localization.LocaleManager
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: AppViewModel,
    onNavigateToShopDetail: (Int) -> Unit,
    onNavigateToAddShop: () -> Unit,
    onNavigateToAddNewsJobsEvents: (String) -> Unit
) {
    val isTamil by viewModel.isTamil.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val userName by viewModel.userName.collectAsState()

    var currentTab by remember { mutableStateOf(0) } // 0: Directory, 1: News, 2: Jobs, 3: Events, 4: Settings

    // Calculate dynamic initials for the avatar
    val initials = remember(userName) {
        val list = userName.split(" ").filter { it.isNotEmpty() }
        if (list.isEmpty()) {
            "JD"
        } else if (list.size == 1) {
            list[0].take(2).uppercase()
        } else {
            "" + list[0].first().uppercase() + list[1].first().uppercase()
        }
    }

    Scaffold(
        topBar = {
            // Elegant custom header matching the HTML styling
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Sriperumbudur ",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDark) Color.White else Blue900,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "Connect",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        textDecoration = TextDecoration.Underline
                                    ),
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDark) PrimaryBlueDark else Blue500,
                                    fontSize = 20.sp
                                )
                            }
                            Text(
                                text = "ஸ்ரீபெரும்புதூர் கனெக்ட்",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = if (isDark) Slate400 else Color(0xFF94A3B8),
                                fontSize = 10.sp
                            )
                        }

                        // Right side: Avatar and utility buttons
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Quick Language switch
                            IconButton(
                                onClick = { viewModel.toggleLanguage() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Toggle Language",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Dark mode quick switch
                            IconButton(
                                onClick = { viewModel.toggleDarkMode() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = "Toggle Theme",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            // Avatar badge with direct link to profile/settings tab
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (isDark) Color(0xFF1E293B) else Color(0xFFDBEAFE))
                                    .border(2.dp, Color.White, CircleShape)
                                    .clickable { currentTab = 4 }
                                    .shadow(2.dp, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initials,
                                    color = if (isDark) PrimaryBlueDark else Blue600,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Highly rounded curved Bento Navigation Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .shadow(16.dp, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 },
                        icon = { Icon(Icons.Default.Storefront, contentDescription = "Shops") },
                        label = { Text(LocaleManager.t("Directory", "கடைகள்", isTamil), fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        icon = { Icon(Icons.Default.Newspaper, contentDescription = "News") },
                        label = { Text(LocaleManager.t("News Dashboard", "செய்தி", isTamil), fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 2,
                        onClick = { currentTab = 2 },
                        icon = { Icon(Icons.Default.WorkOutline, contentDescription = "Jobs") },
                        label = { Text(LocaleManager.t("Jobs Vacancy", "வேலை", isTamil), fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 3,
                        onClick = { currentTab = 3 },
                        icon = { Icon(Icons.Default.Event, contentDescription = "Events") },
                        label = { Text(LocaleManager.t("Town Events", "நிகழ்வு", isTamil), fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 4,
                        onClick = { currentTab = 4 },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text(LocaleManager.t("Sandbox", "அமைப்பு", isTamil), fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentTab == 0) {
                ExtendedFloatingActionButton(
                    text = { Text(LocaleManager.t("Register Shop", "கடை பதிவு", isTamil), fontSize = 13.sp) },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Register Shop Icon") },
                    onClick = { onNavigateToAddShop() },
                    shape = RoundedCornerShape(20.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("register_shop_fab")
                )
            } else if (userRole == "Admin" && (currentTab == 1 || currentTab == 2 || currentTab == 3)) {
                val type = when (currentTab) {
                    1 -> "News"
                    2 -> "Jobs"
                    else -> "Events"
                }
                ExtendedFloatingActionButton(
                    text = { 
                        Text(
                            text = when (currentTab) {
                                1 -> LocaleManager.t("Post News", "செய்தியைப் போடு", isTamil)
                                2 -> LocaleManager.t("Post Job", "வேலையைப் போடு", isTamil)
                                else -> LocaleManager.t("Post Event", "நிகழ்வைப் போடு", isTamil)
                            },
                            fontSize = 13.sp
                        )
                    },
                    icon = { Icon(Icons.Default.PostAdd, contentDescription = "Admin Add Icon") },
                    onClick = { onNavigateToAddNewsJobsEvents(type) },
                    shape = RoundedCornerShape(20.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> ShopsTab(viewModel, onNavigateToShopDetail, onNavigateToAddShop, { tabIndex -> currentTab = tabIndex })
                1 -> NewsTab(viewModel)
                2 -> JobsTab(viewModel)
                3 -> EventsTab(viewModel)
                4 -> SettingsTab(viewModel, onNavigateToShopDetail)
            }
        }
    }
}

@Composable
fun ShopsTab(
    viewModel: AppViewModel, 
    onNavigateToShopDetail: (Int) -> Unit,
    onNavigateToAddShop: () -> Unit,
    onTabSwitch: (Int) -> Unit
) {
    val isTamil by viewModel.isTamil.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val selectedCat by viewModel.selectedCategory.collectAsState()
    val selectedArea by viewModel.selectedArea.collectAsState()
    val shopList by viewModel.filteredShops.collectAsState()
    val userRole by viewModel.userRole.collectAsState()

    val allNews by viewModel.allNews.collectAsState()
    val allJobs by viewModel.allJobs.collectAsState()
    val allEvents by viewModel.allEvents.collectAsState()

    val categories = listOf("All", "Restaurant", "Supermarket", "Medical", "Industry", "Education", "Hospital", "Other")
    val areas = listOf("All", "SIPCOT", "Bus Stand", "Sunguvarchatram", "Theradi", "Oragadam", "Pennalur", "Town Center")

    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar (Styled Bento-style box)
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text(LocaleManager.t("Search shops, products or details...", "கடைகள் அல்லது பொருட்களைத் தேடுக...", isTamil)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = MaterialTheme.colorScheme.primary) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("shop_search_field"),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = if (isDark) SurfaceDark else Color.White,
                focusedContainerColor = if (isDark) SurfaceDark else Color.White
            ),
            shape = RoundedCornerShape(20.dp)
        )

        // Main Scrollable area
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Bento Grid Feature Dashboard - Only visible on clean main view
            val isClean = query.isEmpty() && selectedCat == "All" && selectedArea == "All"
            
            if (isClean) {
                item {
                    BentoGridDashboard(
                        isTamil = isTamil,
                        isDark = isDark,
                        shopCount = shopList.size,
                        jobsCount = allJobs.size,
                        newsList = allNews,
                        eventsList = allEvents,
                        onShopsSelect = { /* Focus on shops catalog below */ },
                        onJobsSelect = { onTabSwitch(2) },
                        onEventsSelect = { onTabSwitch(3) },
                        onQuickClinicFilter = { viewModel.setSelectedCategory("Hospital") },
                        onQuickTrafficFilter = { 
                            onTabSwitch(1) // News Tab
                            // Highlight traffic news
                        },
                        onRegisterBusiness = onNavigateToAddShop
                    )
                }
            }

            // Quick Category Filter Header & Row
            item {
                Text(
                    text = LocaleManager.t("Filter by Category", "பிரிவுவாரியாகத் தேடுக", isTamil),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                    color = if (isDark) Color.White else Blue900
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val displayCat = when (cat) {
                            "All" -> LocaleManager.t("All", "அனைத்தும்", isTamil)
                            "Restaurant" -> LocaleManager.t("Hotels", "உணவகங்கள்", isTamil)
                            "Supermarket" -> LocaleManager.t("Groceries", "மளிகை", isTamil)
                            "Medical" -> LocaleManager.t("Medical", "மருந்தகம்", isTamil)
                            "Industry" -> LocaleManager.t("Industries", "தொழிற்சாலை", isTamil)
                            "Education" -> LocaleManager.t("Colleges/Schools", "கல்வி", isTamil)
                            "Hospital" -> LocaleManager.t("Hospitals", "மருத்துவமனை", isTamil)
                            else -> LocaleManager.t("Others", "மற்றவை", isTamil)
                        }
                        FilterChip(
                            selected = selectedCat == cat,
                            onClick = { viewModel.setSelectedCategory(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            ),
                            label = { Text(displayCat, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }

            // Area Filter Row
            item {
                Text(
                    text = LocaleManager.t("Filter by Area", "பகுதிவாரியாகத் தேடுக", isTamil),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                    color = if (isDark) Color.White else Blue900
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    areas.forEach { area ->
                        val displayArea = if (area == "All") LocaleManager.t("All Areas", "அனைத்து பகுதி", isTamil) else area
                        FilterChip(
                            selected = selectedArea == area,
                            onClick = { viewModel.setSelectedArea(area) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            ),
                            label = { Text(displayArea, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }

            // Directory Catalog Label & Action stats
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = LocaleManager.t("Local Businesses Directory", "உள்ளூர் வணிகங்கள் பட்டியல்", isTamil),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDark) Color.White else Slate900
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${shopList.size} ${LocaleManager.t("found", "கடைகள்", isTamil)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                        if (userRole == "Admin") {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ADMIN",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            if (shopList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = "No shops",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = LocaleManager.t("No businesses found matching criteria", "பொருந்தக்கூடிய கடைகள் ஏதும் இல்லை", isTamil),
                                color = MaterialTheme.colorScheme.outline,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            } else {
                items(shopList) { shop ->
                    ShopCardItem(shop, viewModel, onNavigateToShopDetail)
                }
            }
        }
    }
}

// Spectacular custom Bento Grid visual dashboard representation
@Composable
fun BentoGridDashboard(
    isTamil: Boolean,
    isDark: Boolean,
    shopCount: Int,
    jobsCount: Int,
    newsList: List<NewsEntity>,
    eventsList: List<EventEntity>,
    onShopsSelect: () -> Unit,
    onJobsSelect: () -> Unit,
    onEventsSelect: () -> Unit,
    onQuickClinicFilter: () -> Unit,
    onQuickTrafficFilter: () -> Unit,
    onRegisterBusiness: () -> Unit
) {
    // 1. Extract dynamic headline if possible. Otherwise fall back to Planning connectivity news bulletin
    val latestNewsHeadline = remember(newsList) {
        val announcement = newsList.firstOrNull()
        if (announcement != null) announcement.title to announcement.category else ("New Metro Connectivity planned for Sriperumbudur Hub" to "Traffic")
    }

    // 2. Extract latest featured event
    val featuredEvent = remember(eventsList) {
        val event = eventsList.firstOrNull()
        if (event != null) {
            Triple(event.date, event.title, event.description)
        } else {
            Triple("JUL 15", "Temple Festival", "Adi Krithigai Celebrations")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Card 1: Local Breaking News (Large horizontal gradient block with rounded 28dp)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onJobsSelect() }
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Blue600, Indigo700)
                        )
                    )
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(50.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "BREAKING NEWS".uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = latestNewsHeadline.second.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = latestNewsHeadline.first,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Source: Town Planning Authority Bulletin",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Row containing Categories count grid (Shops Card & Jobs Card side by side)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Shops Bento Card (Orange Theme)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(130.dp)
                    .clickable { onShopsSelect() }
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) SurfaceDark else Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(if (isDark) Color(0xFF45220A) else OrangeBg, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = "Shops",
                            tint = OrangeText,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = LocaleManager.t("Shops Directory", "வணிகங்கள்", isTamil),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Black,
                            color = if (isDark) Color.White else Slate900
                        )
                        Text(
                            text = "${shopCount} ${LocaleManager.t("Registered", "பதிவு செய்யப்பட்டவை", isTamil)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Slate400,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Jobs Bento Card (Green Theme)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(130.dp)
                    .clickable { onJobsSelect() }
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) SurfaceDark else Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(if (isDark) Color(0xFF0F3E21) else GreenBg, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkOutline,
                            contentDescription = "Jobs",
                            tint = GreenText,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = LocaleManager.t("Employment Jobs", "வேலைவாய்ப்புகள்", isTamil),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Black,
                            color = if (isDark) Color.White else Slate900
                        )
                        Text(
                            text = "${jobsCount} ${LocaleManager.t("New Vacancies", "காலியிடங்கள்", isTamil)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Slate400,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Row containing Event (pink card) and Column 2 (Square services + Register Business banner)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left Column: Featured Event (Vertical pink styled card)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(180.dp)
                    .clickable { onEventsSelect() }
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF3F1321) else PinkBg
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = LocaleManager.t("Town Events", "உள்ளூர் நிகழ்வுகள்", isTamil),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Black,
                        color = PinkText
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = featuredEvent.first.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = PinkText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = featuredEvent.second,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Text(
                        text = featuredEvent.third,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isDark) Color(0xFFF43F5E) else PinkText.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Right Column: Stack of Grid Services and Register business
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(180.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Nested Row of Quick Services
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Service A: Clinic
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(84.dp)
                            .clickable { onQuickClinicFilter() }
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) SurfaceDark else Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalHospital,
                                contentDescription = "Hospital",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = LocaleManager.t("CLINIC", "சிகிச்சை", isTamil),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                fontSize = 8.sp,
                                color = Slate400
                            )
                        }
                    }

                    // Service B: Traffic News
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(84.dp)
                            .clickable { onQuickTrafficFilter() }
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) SurfaceDark else Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Navigation,
                                contentDescription = "Traffic",
                                tint = Color(0xFFEAB308),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = LocaleManager.t("TRAFFIC", "நெரிசல்", isTamil),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                fontSize = 8.sp,
                                color = Slate400
                            )
                        }
                    }
                }

                // Register Business (Join Network block styled with dark slate)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)
                        .clickable { onRegisterBusiness() }
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF1E293B) else Color(0xFF1E293B)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = LocaleManager.t("REGISTER SHOP", "கடை பதிவு", isTamil),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                fontSize = 8.sp,
                                color = Blue500
                            )
                            Text(
                                text = LocaleManager.t("Join the network", "இணைந்திடுங்க", isTamil),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "Register",
                            tint = Blue500,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShopCardItem(shop: ShopEntity, viewModel: AppViewModel, onNavigateToShopDetail: (Int) -> Unit) {
    val isTamil by viewModel.isTamil.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()
    val favorites by viewModel.userFavorites.collectAsState()
    val isFav = favorites.any { it.shopId == shop.id }

    val categoryColor = when (shop.category) {
        "Restaurant" -> Color(0xFFFF9800)
        "Supermarket" -> Color(0xFF4CAF50)
        "Medical" -> Color(0xFFE91E63)
        "Industry" -> Color(0xFF607D8B)
        "Education" -> Color(0xFF3F51B5)
        "Hospital" -> Color(0xFF00BCD4)
        else -> Color(0xFF9C27B0)
    }

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onNavigateToShopDetail(shop.id) }
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp))
            .testTag("shop_card_${shop.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) SurfaceDark else Color.White
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(categoryColor)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = shop.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = categoryColor,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = LocaleManager.t(shop.name, shop.tamilName, isTamil),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = if (isDark) Color.White else Slate900,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    IconButton(onClick = { viewModel.toggleFavorite(shop.id) }) {
                        Icon(
                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite Toggle",
                            tint = if (isFav) Color.Red else MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Detail Items Row: Location On
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = if (isDark) Slate400 else Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${shop.area} • ${shop.address}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Detail Items Row: Labels list
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Label,
                        contentDescription = "Products",
                        tint = if (isDark) Slate400 else Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = shop.productsText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = Color(0xFFEAB308),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", shop.rating),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDark) Color.White else Slate900
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(${shop.ratingCount})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )

                        if (shop.hasHomeDelivery) {
                            Spacer(modifier = Modifier.width(12.dp))
                            Box(
                                modifier = Modifier
                                    .background(if (isDark) Color(0xFF0F3E21) else Color(0xFFDCFCE7), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = LocaleManager.t("Home Delivery", "வீட்டு டெலிவரி", isTamil),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isDark) Color(0xFF4ADE80) else Color(0xFF15803D)
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${shop.contactNo}"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .background(if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        if (shop.whatsappNo.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    val url = "https://api.whatsapp.com/send?phone=91${shop.whatsappNo}"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9), CircleShape)
                        ) {
                                Icon(
                                    imageVector = Icons.Default.Message,
                                    contentDescription = "WhatsApp",
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                if (!shop.isApprovedContent) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = LocaleManager.t("⚠️ Pending Admin Approval", "⚠️ நிர்வாகியின் ஒப்புதலுக்கு காத்திருக்கிறது", isTamil),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NewsTab(viewModel: AppViewModel) {
    val isTamil by viewModel.isTamil.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()
    val newsList by viewModel.allNews.collectAsState()
    val userRole by viewModel.userRole.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        // Bento Style Bulletin Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF1D1B4B) else Color(0xFFEEF2F6)
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = LocaleManager.t("Sriperumbudur Alerts Board", "ஸ்ரீபெரும்புதூர் அறிவிப்புப் பலகை", isTamil),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = if (isDark) Color.White else Blue900
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = LocaleManager.t("Get instant verified traffic diversion, government notices and temple festivals bulletins.", "நம்பகமான உள்ளூர் செய்திகள், போக்குவரத்து மாற்றங்கள் மற்றும் அரசாங்க அறிவிப்புகளை உடனுக்குடன் தெரிந்துகொள்ளுங்கள்.", isTamil),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }

        if (newsList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(LocaleManager.t("No active news notifications.", "செய்திகள் ஏதுமில்லை.", isTamil))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(newsList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) SurfaceDark else Color.White
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val categoryText = when (item.category) {
                                    "Traffic" -> LocaleManager.t("Traffic Divert", "போக்குவரத்து", isTamil)
                                    "Government" -> LocaleManager.t("Govt Announcement", "அரசு அறிவிப்பு", isTamil)
                                    else -> LocaleManager.t("General Announcement", "பொது செய்தி", isTamil)
                                }
                                val categoryColor = when (item.category) {
                                    "Traffic" -> Color(0xFFFF5722)
                                    "Government" -> Color(0xFF009688)
                                    else -> Color(0xFF2196F3)
                                }
                                Box(
                                    modifier = Modifier
                                        .background(categoryColor.copy(alpha = 0.15f), RoundedCornerShape(50.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = categoryText.uppercase(),
                                        color = categoryColor,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 9.sp
                                    )
                                }

                                if (userRole == "Admin") {
                                    IconButton(
                                        onClick = { viewModel.deleteNews(item) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = LocaleManager.t(item.title, item.tamilTitle, isTamil),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = if (isDark) Color.White else Slate900
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = LocaleManager.t(item.content, item.tamilContent, isTamil),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "${LocaleManager.t("Posted by: ", "வெளியிட்டவர்: ", isTamil)}${item.author}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Slate400,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JobsTab(viewModel: AppViewModel) {
    val isTamil by viewModel.isTamil.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()
    val jobsList by viewModel.allJobs.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        // Employment Header bulletin
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF022C22) else Color(0xFFF0FDF4)
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = LocaleManager.t("SIPCOT & Factory Employment", "சிப்காட் & தொழிற்சாலை வேலைவாய்ப்பு", isTamil),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = if (isDark) Color.White else Color(0xFF15803D)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = LocaleManager.t("Discover vacancies in Samsung, Foxconn, Salcomp and other major automotive factories in Sriperumbudur.", "சாம்சங், பாக்ஸ்கான், சால்காம்ப் மற்றும் ஸ்ரீபெரும்புதூரில் உள்ள முன்னணி நிறுவனங்களின் வேலைவாய்ப்புகள்.", isTamil),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }

        if (jobsList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(LocaleManager.t("No active job postings.", "வேலைவாய்ப்புகள் ஏதுமில்லை.", isTamil))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(jobsList) { job ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) SurfaceDark else Color.White
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = job.companyName,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = if (isDark) PrimaryBlueDark else Blue600,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = job.jobType.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Slate400,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (userRole == "Admin") {
                                    IconButton(
                                        onClick = { viewModel.deleteJob(job) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = LocaleManager.t(job.title, job.tamilTitle, isTamil),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = if (isDark) Color.White else Slate900
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = LocaleManager.t(job.description, job.tamilDescription, isTamil),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "💰 ${job.salaryRange}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDark) Color.White else Slate900
                                )
                                Text(
                                    text = "📍 ${job.location}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isDark) Color.White else Slate900,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${job.contactPhone}"))
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(LocaleManager.t("Call HR Contact", "அழைக்கவும்", isTamil), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                if (job.email.isNotEmpty()) {
                                    OutlinedButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${job.email}"))
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.Email, contentDescription = "Mail", modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(LocaleManager.t("Email Resume CV", "மின்னஞ்சல்", isTamil), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventsTab(viewModel: AppViewModel) {
    val isTamil by viewModel.isTamil.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()
    val eventsList by viewModel.allEvents.collectAsState()
    val userRole by viewModel.userRole.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        // Events Tab bulletin
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF450A0A) else Color(0xFFFEF2F2)
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = LocaleManager.t("Town Events & Festivals", "முக்கிய நிகழ்வுகள் & விழாக்கள்", isTamil),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = if (isDark) Color.White else Color(0xFF991B1B)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = LocaleManager.t("Keep up with Lord Adikesava Temple processions, Rajiv Gandhi Memorial public campaigns and free local welfare initiatives.", "ஆதிகேசவர் கோவில் பிரம்மோற்சவம், ராஜிவ் காந்தி நினைவு இல்ல நிகழ்வுகள் மற்றும் சமூக நல வாய்ப்புகள்.", isTamil),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }

        if (eventsList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(LocaleManager.t("No upcoming local events.", "வரவிருக்கும் நிகழ்வுகள் ஏதுமில்லை.", isTamil))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(eventsList) { evt ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) SurfaceDark else Color.White
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(if (isDark) Color(0xFF450A22) else Color(0xFFFFF1F2), RoundedCornerShape(50.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "📅 ${evt.date}".uppercase(),
                                        color = PinkText,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 10.sp
                                    )
                                }

                                if (userRole == "Admin") {
                                    IconButton(
                                        onClick = { viewModel.deleteEvent(evt) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "⏰ ${evt.time}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = LocaleManager.t(evt.title, evt.tamilTitle, isTamil),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = if (isDark) Color.White else Slate900
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = LocaleManager.t(evt.description, evt.tamilDescription, isTamil),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "📍 ${LocaleManager.t("Venue: ", "இடம்: ", isTamil)}${evt.venue}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isDark) PrimaryBlueDark else Blue600
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "🗣️ ${LocaleManager.t("Organizer: ", "ஏற்பாட்டாளர்: ", isTamil)}${evt.organizer}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Slate400,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsTab(viewModel: AppViewModel, onNavigateToShopDetail: (Int) -> Unit) {
    val isTamil by viewModel.isTamil.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()
    val currentRole by viewModel.userRole.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()
    val allShops by viewModel.allShops.collectAsState()

    var editingProfile by remember { mutableStateOf(false) }
    var inputName by remember { mutableStateOf(userName) }
    var inputPhone by remember { mutableStateOf(userPhone) }

    // Filter pending shops for admin
    val pendingShops = allShops.filter { !it.isApprovedContent }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            Text(
                text = LocaleManager.t("User Profile & Configuration", "சுயவிவரம் மற்றும் அமைப்புகள்", isTamil),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = if (isDark) Color.White else Blue900
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // 1. User Profile Details Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) SurfaceDark else Color.White
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile Pic",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = userName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else Slate900
                                )
                                Text(
                                    text = userPhone,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate400
                                )
                            }
                        }
                        IconButton(onClick = { editingProfile = !editingProfile }) {
                            Icon(
                                imageVector = if (editingProfile) Icons.Default.Close else Icons.Default.Edit,
                                contentDescription = "Edit Profile"
                            )
                        }
                    }

                    if (editingProfile) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = inputName,
                            onValueChange = { inputName = it },
                            label = { Text(LocaleManager.t("Your Name", "உங்கள் பெயர்", isTamil)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = inputPhone,
                            onValueChange = { inputPhone = it },
                            label = { Text(LocaleManager.t("Your Phone No", "மொபைல் எண்", isTamil)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.updateProfile(inputName, inputPhone)
                                editingProfile = false
                            },
                            modifier = Modifier.align(Alignment.End),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(LocaleManager.t("Save Settings", "சேமி", isTamil), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 2. Playable Role Toggles
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = LocaleManager.t("Simulated Authentication Sandbox", "அங்கீகாரப் போலி சோதனை", isTamil),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Black,
                color = if (isDark) Color.White else Blue900
            )
            Text(
                text = LocaleManager.t("Change roles below instantly to test User bookmarking, Shop Owner dashboard, or Admin content moderation.", "புக்மார்க்குகளைச் சோதிக்க 'பயனர்', சொந்த விவரங்களை மாற்ற 'கடை உரிமையாளர்' அல்லது நிர்வாகப் பலகைக்கு 'நிர்வாகி' என்பதைத் தேர்ந்தெடுக்கவும்.", isTamil),
                style = MaterialTheme.typography.bodySmall,
                color = Slate400,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) SurfaceDark.copy(alpha = 0.5f) else Color.White
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${LocaleManager.t("Current Role: ", "உங்களது தற்போதைய ரோல்: ", isTamil)}${currentRole.uppercase()}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        InputChip(
                            selected = currentRole == "User",
                            onClick = { viewModel.setUserRole("User") },
                            label = { Text(LocaleManager.t("Regular User", "வழக்கமான பயனர்", isTamil), fontWeight = FontWeight.Bold) }
                        )
                        InputChip(
                            selected = currentRole == "Shop Owner",
                            onClick = { viewModel.setUserRole("Shop Owner") },
                            label = { Text(LocaleManager.t("Shop Owner", "கடை உரிமையாளர்", isTamil), fontWeight = FontWeight.Bold) }
                        )
                        InputChip(
                            selected = currentRole == "Admin",
                            onClick = { viewModel.setUserRole("Admin") },
                            label = { Text(LocaleManager.t("City Admin", "நிர்வாகி", isTamil), fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }
        }

        // 3. Settings Switches
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = LocaleManager.t("Preferences", "விருப்பத்தேர்வுகள்", isTamil), 
                style = MaterialTheme.typography.titleSmall, 
                fontWeight = FontWeight.Black,
                color = if (isDark) Color.White else Blue900
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) SurfaceDark else Color.White
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text(LocaleManager.t("Bilingual Mode (Tamil)", "தமிழ் பதிப்பிற்கு மாறவும்", isTamil), fontWeight = FontWeight.Bold) },
                        supportingContent = { Text(LocaleManager.t("Translates all titles and labels", "தமிழ் மற்றும் ஆங்கில மொழிகளுக்கு இடையே மாறுக", isTamil)) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        trailingContent = {
                            Switch(
                                checked = isTamil,
                                onCheckedChange = { viewModel.toggleLanguage() }
                            )
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                    ListItem(
                        headlineContent = { Text(LocaleManager.t("Dark Palette Colors", "டார்க் தீம்", isTamil), fontWeight = FontWeight.Bold) },
                        supportingContent = { Text(LocaleManager.t("Better screen comfort at night", "வசதியான இரவு காட்சி மோடு", isTamil)) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        trailingContent = {
                            Switch(
                                checked = isDark,
                                onCheckedChange = { viewModel.toggleDarkMode() }
                            )
                        }
                    )
                }
            }
        }

        // 4. Admin Approvals section if user is admin
        if (currentRole == "Admin") {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = LocaleManager.t("Pending Registrations (${pendingShops.size})", "ஒப்புதல் தேவைப்படும் கடைகள் (${pendingShops.size})", isTamil),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (pendingShops.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                            Text(
                                LocaleManager.t("No pending shop approvals right now.", "அங்கீகாரத்திற்கு எந்த கடைகளும் காத்திருக்கவில்லை.", isTamil),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            } else {
                items(pendingShops) { pShop ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp)),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = LocaleManager.t(pShop.name, pShop.tamilName, isTamil),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Slate900
                            )
                            Text(
                                text = "${pShop.category} • ${pShop.area}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate400,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = pShop.address,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.approveShop(pShop) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text(LocaleManager.t("Approve Shop", "அங்கீகரி", isTamil), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.deleteShop(pShop) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text(LocaleManager.t("Reject", "நிராகரி", isTamil), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

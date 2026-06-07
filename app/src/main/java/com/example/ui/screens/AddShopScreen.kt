package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppViewModel
import com.example.ui.localization.LocaleManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShopScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val isTamil by viewModel.isTamil.collectAsState()

    var name by remember { mutableStateOf("") }
    var tamilName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Restaurant") }
    var address by remember { mutableStateOf("") }
    var contactNo by remember { mutableStateOf("") }
    var whatsappNo by remember { mutableStateOf("") }
    var mapLocationUrl by remember { mutableStateOf("") }
    var openingTime by remember { mutableStateOf("09:00 AM") }
    var closingTime by remember { mutableStateOf("09:00 PM") }
    var offers by remember { mutableStateOf("") }
    var productsText by remember { mutableStateOf("") }
    var hasHomeDelivery by remember { mutableStateOf(false) }
    var area by remember { mutableStateOf("Town Center") }

    var categoryDropdownExp by remember { mutableStateOf(false) }
    var areaDropdownExp by remember { mutableStateOf(false) }

    val categories = listOf("Restaurant", "Supermarket", "Medical", "Industry", "Education", "Hospital", "Other")
    val areas = listOf("SIPCOT", "Bus Stand", "Sunguvarchatram", "Theradi", "Oragadam", "Pennalur", "Town Center")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(LocaleManager.t("Register Business", "புதிய கடையைப் பதிவு செய்க", isTamil), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("add_shop_form_scroll"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = LocaleManager.t("Business Portal Registration", "வியாபார சுயவிவரத் தகவல்கள்", isTamil),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Shop Name English
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(LocaleManager.t("Shop Name (English)", "கடையின் பெயர் (ஆங்கிலம்)", isTamil)) },
                modifier = Modifier.fillMaxWidth().testTag("add_shop_input_name_en"),
                singleLine = true
            )

            // Shop Name Tamil
            OutlinedTextField(
                value = tamilName,
                onValueChange = { tamilName = it },
                label = { Text(LocaleManager.t("Shop Name (Tamil)", "கடையின் பெயர் (தமிழ்)", isTamil)) },
                modifier = Modifier.fillMaxWidth().testTag("add_shop_input_name_ta"),
                singleLine = true
            )

            // Category Drops Selection
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(LocaleManager.t("Business Category", "வணிகப் பிரிவு", isTamil)) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "DropdownIcon",
                            modifier = Modifier.clickable { categoryDropdownExp = true }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().clickable { categoryDropdownExp = true }
                )
                DropdownMenu(
                    expanded = categoryDropdownExp,
                    onDismissRequest = { categoryDropdownExp = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    categories.forEach { cat ->
                        val displayCatName = when (cat) {
                            "Restaurant" -> LocaleManager.t("Hotels / Restaurant", "உணவகங்கள்", isTamil)
                            "Supermarket" -> LocaleManager.t("Groceries / Supermarket", "மளிகைக் கடை", isTamil)
                            "Medical" -> LocaleManager.t("Medical / Pharmacy", "மருந்தகம்", isTamil)
                            "Industry" -> LocaleManager.t("SIPCOT Industry", "தொழிற்சாலை", isTamil)
                            "Education" -> LocaleManager.t("Education Campus", "கல்வி", isTamil)
                            "Hospital" -> LocaleManager.t("Hospital / Clinic", "மருத்துவமனை", isTamil)
                            else -> LocaleManager.t("Other Miscellaneous", "மற்றவை", isTamil)
                        }
                        DropdownMenuItem(
                            text = { Text(displayCatName) },
                            onClick = {
                                category = cat
                                categoryDropdownExp = false
                            }
                        )
                    }
                }
            }

            // Region Area Landmarks
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = area,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(LocaleManager.t("Select Town Area", "நகரப் பகுதி", isTamil)) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "DropdownIcon",
                            modifier = Modifier.clickable { areaDropdownExp = true }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().clickable { areaDropdownExp = true }
                )
                DropdownMenu(
                    expanded = areaDropdownExp,
                    onDismissRequest = { areaDropdownExp = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    areas.forEach { ar ->
                        DropdownMenuItem(
                            text = { Text(ar) },
                            onClick = {
                                area = ar
                                areaDropdownExp = false
                            }
                        )
                    }
                }
            }

            // Shop full address
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(LocaleManager.t("Full Address / Landmark details", "முழு முகவரி விவரங்கள்", isTamil)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )

            // Contact Number
            OutlinedTextField(
                value = contactNo,
                onValueChange = { contactNo = it },
                label = { Text(LocaleManager.t("Primary Business Phone No", "தொடர்பு மொபைல் எண்", isTamil)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // WhatsApp Number
            OutlinedTextField(
                value = whatsappNo,
                onValueChange = { whatsappNo = it },
                label = { Text(LocaleManager.t("WhatsApp Business Number", "வாட்ஸ்அப் எண்", isTamil)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Google Maps URL linkage
            OutlinedTextField(
                value = mapLocationUrl,
                onValueChange = { mapLocationUrl = it },
                label = { Text(LocaleManager.t("Google Map URL Link (Optional)", "கூகுள் மேப் வழித்தட லிங்க்", isTamil)) },
                placeholder = { Text("https://maps.google.com/?q=...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Opening Time
            OutlinedTextField(
                value = openingTime,
                onValueChange = { openingTime = it },
                label = { Text(LocaleManager.t("Opening Hour Time (e.g. 09:00 AM)", "நிறுவனம் திறக்கும் நேரம்", isTamil)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Closing Time
            OutlinedTextField(
                value = closingTime,
                onValueChange = { closingTime = it },
                label = { Text(LocaleManager.t("Closing Hour Time (e.g. 09:00 PM)", "நிறுவனம் மூடும் நேரம்", isTamil)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Products Describing Text snippet
            OutlinedTextField(
                value = productsText,
                onValueChange = { productsText = it },
                label = { Text(LocaleManager.t("Products Listing Snippet (e.g. Rice, Idly)", "நிறுவனத்தில் கிடைக்கும் பொருட்கள் சுருக்கம்", isTamil)) },
                placeholder = { Text("Apples, Books, Medicine...") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )

            // Active Special Discounts / Offers
            OutlinedTextField(
                value = offers,
                onValueChange = { offers = it },
                label = { Text(LocaleManager.t("Discounts / Offers details (Optional)", "வழங்கும் சலுகைகள் விவரங்கள்", isTamil)) },
                placeholder = { Text("10% Discount on ₹2000 purchases!") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )

            // Home delivery checks
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(LocaleManager.t("Is Home Delivery Service Available?", "வீட்டு விநியோக வசதி உள்ளதா?", isTamil), fontWeight = FontWeight.Medium)
                Switch(checked = hasHomeDelivery, onCheckedChange = { hasHomeDelivery = it })
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Submit registration buttons
            Button(
                onClick = {
                    if (name.isNotEmpty() && contactNo.isNotEmpty()) {
                        val validatedMapUrl = if (mapLocationUrl.isNotEmpty()) mapLocationUrl else "https://maps.google.com/?q=Sriperumbudur+$name"
                        viewModel.registerShop(
                            name = name,
                            tamilName = tamilName.ifEmpty { name },
                            category = category,
                            address = address.ifEmpty { "Sriperumbudur Town, Kancheepuram" },
                            contactNo = contactNo,
                            whatsappNo = whatsappNo,
                            mapLocationUrl = validatedMapUrl,
                            openingTime = openingTime,
                            closingTime = closingTime,
                            imageUrl = "shop_default",
                            productsText = productsText.ifEmpty { "General listings available" },
                            offers = offers,
                            hasHomeDelivery = hasHomeDelivery,
                            area = area,
                            isOwner = true
                        )
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_shop_registration_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(LocaleManager.t("Submit Shop Registration", "பதிவைச் சமர்ப்பி", isTamil), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            
            Box(
                modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = LocaleManager.t("Note: User self-registrations will show once approved by town administrator.", "குறிப்பு: உங்களது கடையானது நகர நிர்வாகி ஒப்புதல் அளித்தவுடன் பயன்பாட்டிற்கு வரும்.", isTamil),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

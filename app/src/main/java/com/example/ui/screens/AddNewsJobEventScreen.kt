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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppViewModel
import com.example.ui.localization.LocaleManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewsJobEventScreen(
    type: String, // "News", "Jobs", "Events"
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val isTamil by viewModel.isTamil.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val heading = when (type) {
                        "News" -> LocaleManager.t("Post Local News", "செய்தியைப் போடுக", isTamil)
                        "Jobs" -> LocaleManager.t("Post Job Vacancy", "வேலைவாய்ப்பை வெளியிடு", isTamil)
                        else -> LocaleManager.t("Post Town Event", "நிகழ்வை வெளியிடு", isTamil)
                    }
                    Text(heading, fontWeight = FontWeight.Bold)
                },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (type) {
                "News" -> NewsPostForm(viewModel, onNavigateBack, isTamil)
                "Jobs" -> JobsPostForm(viewModel, onNavigateBack, isTamil)
                else -> EventsPostForm(viewModel, onNavigateBack, isTamil)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsPostForm(viewModel: AppViewModel, onBack: () -> Unit, isTamil: Boolean) {
    var titleEn by remember { mutableStateOf("") }
    var titleTa by remember { mutableStateOf("") }
    var contentEn by remember { mutableStateOf("") }
    var contentTa by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("General") }
    var catDropdownExp by remember { mutableStateOf(false) }

    val categories = listOf("General", "Traffic", "Government")

    OutlinedTextField(
        value = titleEn,
        onValueChange = { titleEn = it },
        label = { Text(LocaleManager.t("News Title (English)", "செய்தியின் தட்டச்சு (ஆங்கிலம்)", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = titleTa,
        onValueChange = { titleTa = it },
        label = { Text(LocaleManager.t("News Title (Tamil)", "செய்தியின் தட்டச்சு (தமிழ்)", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = category,
            onValueChange = {},
            readOnly = true,
            label = { Text(LocaleManager.t("News Category", "செய்திப் பிரிவு", isTamil)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "DropdownIcon",
                    modifier = Modifier.clickable { catDropdownExp = true }
                )
            },
            modifier = Modifier.fillMaxWidth().clickable { catDropdownExp = true }
        )
        DropdownMenu(expanded = catDropdownExp, onDismissRequest = { catDropdownExp = false }) {
            categories.forEach { cat ->
                DropdownMenuItem(
                    text = { Text(cat) },
                    onClick = {
                        category = cat
                        catDropdownExp = false
                    }
                )
            }
        }
    }

    OutlinedTextField(
        value = contentEn,
        onValueChange = { contentEn = it },
        label = { Text(LocaleManager.t("Content description (English)", "செய்தியின் முக்கிய விவரம் (ஆங்கிலம்)", isTamil)) },
        modifier = Modifier.fillMaxWidth().height(120.dp)
    )

    OutlinedTextField(
        value = contentTa,
        onValueChange = { contentTa = it },
        label = { Text(LocaleManager.t("Content description (Tamil)", "செய்தியின் முக்கிய விவரம் (தமிழ்)", isTamil)) },
        modifier = Modifier.fillMaxWidth().height(120.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    Button(
        onClick = {
            if (titleEn.isNotEmpty() && contentEn.isNotEmpty()) {
                viewModel.addLocalNews(titleEn, titleTa.ifEmpty { titleEn }, contentEn, contentTa.ifEmpty { contentEn }, category)
                onBack()
            }
        },
        modifier = Modifier.fillMaxWidth().height(50.dp).testTag("news_submit_button"),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(LocaleManager.t("Publish News Alert", "செய்தியைப் பிரசுரி", isTamil), fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun JobsPostForm(viewModel: AppViewModel, onBack: () -> Unit, isTamil: Boolean) {
    var company by remember { mutableStateOf("") }
    var titleEn by remember { mutableStateOf("") }
    var titleTa by remember { mutableStateOf("") }
    var descEn by remember { mutableStateOf("") }
    var descTa by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Factory") }
    var location by remember { mutableStateOf("SIPCOT") }
    var salary by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    OutlinedTextField(
        value = company,
        onValueChange = { company = it },
        label = { Text(LocaleManager.t("Hiring Company Name (e.g. Foxconn)", "நிறுவனத்தின் பெயர்", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = titleEn,
        onValueChange = { titleEn = it },
        label = { Text(LocaleManager.t("Job Title (English)", "வேலைத் தலைப்பு (ஆங்கிலம்)", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = titleTa,
        onValueChange = { titleTa = it },
        label = { Text(LocaleManager.t("Job Title (Tamil)", "வேலைத் தலைப்பு (தமிழ்)", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = descEn,
        onValueChange = { descEn = it },
        label = { Text(LocaleManager.t("Requirements/Description (English)", "தகுதிகள் மற்றும் விவரங்கள் (ஆங்கிலம்)", isTamil)) },
        modifier = Modifier.fillMaxWidth().height(100.dp)
    )

    OutlinedTextField(
        value = descTa,
        onValueChange = { descTa = it },
        label = { Text(LocaleManager.t("Requirements/Description (Tamil)", "தகுதிகள் மற்றும் விவரங்கள் (தமிழ்)", isTamil)) },
        modifier = Modifier.fillMaxWidth().height(100.dp)
    )

    OutlinedTextField(
        value = salary,
        onValueChange = { salary = it },
        label = { Text(LocaleManager.t("Salary package (e.g. ₹18,000)", "சம்பள விவரம்", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = location,
        onValueChange = { location = it },
        label = { Text(LocaleManager.t("Job Location (e.g. SIPCOT Area)", "வேலை இடம்", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        label = { Text(LocaleManager.t("HR Mobile Number", "தொடர்பு கொள்ள வேண்டிய போன் எண்", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text(LocaleManager.t("HR Email (Optional)", "மின்னஞ்சல் முகவரி", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(10.dp))

    Button(
        onClick = {
            if (company.isNotEmpty() && titleEn.isNotEmpty() && phone.isNotEmpty()) {
                viewModel.addJobVacancy(
                    company = company,
                    title = titleEn,
                    tamilTitle = titleTa.ifEmpty { titleEn },
                    desc = descEn,
                    tamilDesc = descTa.ifEmpty { descEn },
                    type = type,
                    location = location,
                    salary = salary.ifEmpty { "Salary Not Disclosed" },
                    phone = phone,
                    email = email
                )
                onBack()
            }
        },
        modifier = Modifier.fillMaxWidth().height(50.dp).testTag("jobs_submit_button"),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(LocaleManager.t("Post Job Opportunity", "வேலைவாய்ப்பை வெளியிடு", isTamil), fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun EventsPostForm(viewModel: AppViewModel, onBack: () -> Unit, isTamil: Boolean) {
    var titleEn by remember { mutableStateOf("") }
    var titleTa by remember { mutableStateOf("") }
    var descEn by remember { mutableStateOf("") }
    var descTa by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var venue by remember { mutableStateOf("") }
    var organizer by remember { mutableStateOf("") }

    OutlinedTextField(
        value = titleEn,
        onValueChange = { titleEn = it },
        label = { Text(LocaleManager.t("Event Title (English)", "நிகழ்வுத் தலைப்பு (ஆங்கிலம்)", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = titleTa,
        onValueChange = { titleTa = it },
        label = { Text(LocaleManager.t("Event Title (Tamil)", "நிகழ்வுத் தலைப்பு (தமிழ்)", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = descEn,
        onValueChange = { descEn = it },
        label = { Text(LocaleManager.t("Details description (English)", "நிகழ்வு விளக்கம் (ஆங்கிலம்)", isTamil)) },
        modifier = Modifier.fillMaxWidth().height(100.dp)
    )

    OutlinedTextField(
        value = descTa,
        onValueChange = { descTa = it },
        label = { Text(LocaleManager.t("Details description (Tamil)", "நிகழ்வு விளக்கம் (தமிழ்)", isTamil)) },
        modifier = Modifier.fillMaxWidth().height(100.dp)
    )

    OutlinedTextField(
        value = date,
        onValueChange = { date = it },
        label = { Text(LocaleManager.t("Event Date (e.g. May 15)", "தேதி", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = time,
        onValueChange = { time = it },
        label = { Text(LocaleManager.t("Event Time (e.g. 06 PM onwards)", "நேரம்", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = venue,
        onValueChange = { venue = it },
        label = { Text(LocaleManager.t("Event Venue (Address)", "நடைபெறும் இடம்", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = organizer,
        onValueChange = { organizer = it },
        label = { Text(LocaleManager.t("Organized by", "நடைபெற உதவும் அமைப்பு", isTamil)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(10.dp))

    Button(
        onClick = {
            if (titleEn.isNotEmpty() && venue.isNotEmpty()) {
                viewModel.addTownEvent(
                    title = titleEn,
                    tamilTitle = titleTa.ifEmpty { titleEn },
                    desc = descEn,
                    tamilDesc = descTa.ifEmpty { descEn },
                    date = date.ifEmpty { "TBD" },
                    time = time.ifEmpty { "Full Day" },
                    venue = venue,
                    organizer = organizer.ifEmpty { "Town Committee" }
                )
                onBack()
            }
        },
        modifier = Modifier.fillMaxWidth().height(50.dp).testTag("events_submit_button"),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(LocaleManager.t("Publish Town Event", "நிகழ்வைப் பதிவேற்று", isTamil), fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

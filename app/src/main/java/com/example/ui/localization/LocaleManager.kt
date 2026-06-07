package com.example.ui.localization

object LocaleManager {
    fun t(english: String, tamil: String, isTamil: Boolean): String {
        return if (isTamil) tamil else english
    }

    // Common translation strings
    val appNameEnTa = Pair("Sriperumbudur Connect", "ஸ்ரீபெரும்புதூர் கனெக்ட்")
    val allShopsEnTa = Pair("All Businesses", "அனைத்து கடைகள்")
    val searchPlaceholderEnTa = Pair("Search shops or products...", "கடைகள் அல்லது பொருட்களைத் தேடுக...")
    val categoriesEnTa = Pair("Categories", "பிரிவுகள்")
    val areasEnTa = Pair("Areas", "பகுதிகள்")
    val localNewsEnTa = Pair("Local News", "உள்ளூர் செய்திகள்")
    val jobsEnTa = Pair("Jobs Section", "வேலைவாய்ப்புகள்")
    val eventsEnTa = Pair("Events Board", "நிகழ்வுகள் பலகை")
    val adminPanelEnTa = Pair("Admin Dashboard", "நிர்வாகக் குழு")
    val favoriteShopsEnTa = Pair("My Favorites", "விருப்பமான கடைகள்")
    
    // Shop details
    val addressEnTa = Pair("Address", "முகவரி")
    val contactNoEnTa = Pair("Contact Number", "தொடர்பு எண்")
    val whatsappNoEnTa = Pair("WhatsApp Number", "வாட்ஸ்அப் எண்")
    val openingHoursEnTa = Pair("Opening Hours", "திறக்கும் நேரம்")
    val productsEnTa = Pair("Products Available", "கிடைக்கும் பொருட்கள்")
    val offersEnTa = Pair("Special Offers", "சிறப்பு சலுகைகள்")
    val homeDeliveryEnTa = Pair("Home Delivery", "வீட்டு விநியோகம்")
    val getDirectionsEnTa = Pair("Get Directions", "வழிமுறைகள் (வரைபடம்)")
    val writeReviewEnTa = Pair("Write a Review", "மதிப்புரை எழுதுக")
    val submitReviewEnTa = Pair("Submit Review", "மதிப்புரையை சமர்ப்பி")
    val reviewPlaceholderEnTa = Pair("Type your review here...", "உங்கள் கருத்துக்களை இங்கே தட்டச்சு செய்க...")
    val reviewsTitleEnTa = Pair("Reviews and Ratings", "மதிப்புரைகள் மற்றும் மதிப்பீடுகள்")

    // Buttons / Toggles
    val editShopEnTa = Pair("Edit Shop Details", "கடை விவரங்களை மாற்று")
    val addProductEnTa = Pair("Add Product Item", "தயாரிப்பைச் சேர்")
    val registerBusinessEnTa = Pair("Register Your Business", "புதிய கடையை பதிவு செய்க")
    val darkThemeEnTa = Pair("Dark Mode", "டார்க் மோடு")
    val changeLanguageEnTa = Pair("தமிழ் பதிப்பு", "English Version") // showing language toggle opposite
    
    // Reg form
    val shopNameEnTa = Pair("Shop Name (Eng)", "கடையின் பெயர் (ஆங்கிலம்)")
    val shopNameTaEnTa = Pair("Shop Name (Tamil)", "கடையின் பெயர் (தமிழ்)")
    val selectCategoryEnTa = Pair("Select Category", "பிரிவைத் தேர்ந்தெடுக்கவும்")
    val selectAreaEnTa = Pair("Select Area / Landmark", "நகரப் பகுதியைத் தேர்ந்தெடுக்கவும்")
    val mapLinkEnTa = Pair("Google Map Location URL", "கூகுள் மேப் முகவரி (லிங்க்)")
    val registerBtnEnTa = Pair("Submit Registration", "பதிவை சமர்ப்பி")

    // Admin Dashboard
    val adminApproveBtnEnTa = Pair("Approve Business", "அங்கீகரிக்கவும்")
    val adminModerateReviewsEnTa = Pair("Moderate Reviews", "கருத்துக்களை நிர்வகி")
    val createNoticeEnTa = Pair("Post News Update", "புதிய செய்தி வெளியிடவும்")
    val createJobEnTa = Pair("Post Job Vacancy", "புதிய வேலைவாய்ப்பை வெளியிடவும்")
    val createEventEnTa = Pair("Post Town Event", "புதிய நிகழ்வை வெளியிடவும்")
    val deleteBtnEnTa = Pair("Delete", "நீக்கு")
}

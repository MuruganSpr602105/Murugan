package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ShopEntity::class,
        ProductEntity::class,
        NewsEntity::class,
        JobEntity::class,
        EventEntity::class,
        ReviewEntity::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shopDao(): ShopDao
    abstract fun productDao(): ProductDao
    abstract fun newsDao(): NewsDao
    abstract fun jobDao(): JobDao
    abstract fun eventDao(): EventDao
    abstract fun reviewDao(): ReviewDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sriperumbudur_connect_db"
                )
                .addCallback(DatabaseCallback(context.applicationContext))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Pre-populate data using a background thread when database is created
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                val database = getInstance(context)
                
                // 1. Prepopulate Shops
                val shops = listOf(
                    ShopEntity(
                        name = "Sri Adikesava Perumal Temple Shop",
                        tamilName = "ஸ்ரீ ஆதிகேசவ பெருமாள் கோவில் கடை",
                        category = "Other",
                        address = "Temple Main Street, Theradi, Sriperumbudur",
                        contactNo = "04427162236",
                        whatsappNo = "9444000111",
                        mapLocationUrl = "https://maps.google.com/?q=Adikesava+Perumal+Temple+Sriperumbudur",
                        openingTime = "06:00 AM",
                        closingTime = "08:30 PM",
                        imageUrl = "temple",
                        productsText = "Pooja Items, Temple Prasadam, Devotional Books, Flowers",
                        offers = "Special Pooja Plate Combo at ₹100",
                        hasHomeDelivery = false,
                        isApprovedContent = true,
                        rating = 4.9f,
                        ratingCount = 42,
                        area = "Theradi"
                    ),
                    ShopEntity(
                        name = "Sri Krishna Vilas Restaurant",
                        tamilName = "ஸ்ரீ கிருஷ்ணா விலாஸ் உணவகம்",
                        category = "Restaurant",
                        address = "Gandhi Road, Near Bus Stand, Sriperumbudur",
                        contactNo = "04427163345",
                        whatsappNo = "9840212345",
                        mapLocationUrl = "https://maps.google.com/?q=Sriperumbudur+Bus+Stand",
                        openingTime = "07:00 AM",
                        closingTime = "10:30 PM",
                        imageUrl = "restaurant",
                        productsText = "South Indian Meals, Ghee Roast Dosa, Idly, Filter Coffee",
                        offers = "10% off on bills above ₹500",
                        hasHomeDelivery = true,
                        isApprovedContent = true,
                        rating = 4.5f,
                        ratingCount = 120,
                        area = "Bus Stand"
                    ),
                    ShopEntity(
                        name = "Rasi Super Market",
                        tamilName = "ராசி சூப்பர் மார்க்கெட்",
                        category = "Supermarket",
                        address = "Kancheepuram Road, Sriperumbudur",
                        contactNo = "9176543210",
                        whatsappNo = "9176543210",
                        mapLocationUrl = "https://maps.google.com/?q=Sriperumbudur+Town",
                        openingTime = "08:00 AM",
                        closingTime = "09:30 PM",
                        imageUrl = "supermarket",
                        productsText = "Rice, Dhal, Oils, Groceries, Cosmetics, Fresh Vegetables",
                        offers = "₹50 discount voucher on ₹1000 purchase",
                        hasHomeDelivery = true,
                        isApprovedContent = true,
                        rating = 4.2f,
                        ratingCount = 85,
                        area = "Town Center"
                    ),
                    ShopEntity(
                        name = "Apollo Pharmacy Sriperumbudur",
                        tamilName = "அப்போலோ பார்மசி ஸ்ரீபெரும்புதூர்",
                        category = "Medical",
                        address = "Sunguvarchatram Bypass Road, Sriperumbudur",
                        contactNo = "04427161122",
                        whatsappNo = "9940112233",
                        mapLocationUrl = "https://maps.google.com/?q=Sunguvarchatram",
                        openingTime = "24 Hours",
                        closingTime = "24 Hours",
                        imageUrl = "pharmacy",
                        productsText = "Allopathy Medicines, First Aid, Health Supplements, Baby Products",
                        offers = "Up to 15% discount on prescription medicines",
                        hasHomeDelivery = true,
                        isApprovedContent = true,
                        rating = 4.6f,
                        ratingCount = 50,
                        area = "Sunguvarchatram"
                    ),
                    ShopEntity(
                        name = "Hyundai Motor India Ltd (Factory)",
                        tamilName = "ஹூண்டாய் மோட்டார் இந்தியா (தொழிற்சாலை)",
                        category = "Industry",
                        address = "Plot No. H-1, SIPCOT Industrial Park, Irrungattukottai, Sriperumbudur",
                        contactNo = "04467101000",
                        whatsappNo = "",
                        mapLocationUrl = "https://maps.google.com/?q=Hyundai+Factory+Sriperumbudur",
                        openingTime = "08:00 AM",
                        closingTime = "06:00 PM",
                        imageUrl = "factory",
                        productsText = "Automobile Manufacturing, Global Parts Supplier",
                        offers = "No public offers, official industrial center",
                        hasHomeDelivery = false,
                        isApprovedContent = true,
                        rating = 4.8f,
                        ratingCount = 500,
                        area = "SIPCOT"
                    ),
                    ShopEntity(
                        name = "Samsung Electronics India",
                        tamilName = "சாம்சங் எலக்ட்ரானிக்ஸ் இந்தியா",
                        category = "Industry",
                        address = "SIPCOT Industrial Estate, Sriperumbudur",
                        contactNo = "04467142000",
                        whatsappNo = "",
                        mapLocationUrl = "https://maps.google.com/?q=Samsung+Sriperumbudur",
                        openingTime = "08:30 AM",
                        closingTime = "05:30 PM",
                        imageUrl = "factory_electronics",
                        productsText = "Home Appliances Manufacturing, Smart LED TVs, ACs, Washing Machines",
                        offers = "Authorized industrial facility",
                        hasHomeDelivery = false,
                        isApprovedContent = true,
                        rating = 4.7f,
                        ratingCount = 210,
                        area = "SIPCOT"
                    ),
                    ShopEntity(
                        name = "Sri Venkateswara College of Engineering (SVCE)",
                        tamilName = "ஸ்ரீ வெங்கடேஸ்வரா பொறியியல் கல்லூரி",
                        category = "Education",
                        address = "Pennalur, Sriperumbudur, Kancheepuram Dist",
                        contactNo = "04427152000",
                        whatsappNo = "",
                        mapLocationUrl = "https://maps.google.com/?q=SVCE+Pennalur",
                        openingTime = "08:15 AM",
                        closingTime = "03:30 PM",
                        imageUrl = "college",
                        productsText = "Engineering Degree Programs, MBA, Ph.D, Research Centers",
                        offers = "NAAC A+ Accredited Institution",
                        hasHomeDelivery = false,
                        isApprovedContent = true,
                        rating = 4.4f,
                        ratingCount = 330,
                        area = "Pennalur"
                    )
                )

                // Save Shops & capture IDs to populate products underneath
                for (shop in shops) {
                    val id = database.shopDao().insertShop(shop)
                    
                    // Add products for each shop
                    if (shop.name.contains("Temple")) {
                        database.productDao().insertProduct(ProductEntity(shopId = id.toInt(), name = "Pooja Plate Pack", tamilName = "பூஜை தட்டு தொகுப்பு", price = 100.0, inStock = true))
                        database.productDao().insertProduct(ProductEntity(shopId = id.toInt(), name = "Ghee Diya Pack (50 Pcs)", tamilName = "நெய் தீபம் பேக் (50)", price = 150.0, inStock = true))
                        database.productDao().insertProduct(ProductEntity(shopId = id.toInt(), name = "Special Sandalwood Paste", tamilName = "சிறப்பு சந்தன பேஸ்ட்", price = 60.0, inStock = true))
                    } else if (shop.name.contains("Krishna Vilas")) {
                        database.productDao().insertProduct(ProductEntity(shopId = id.toInt(), name = "Special South Indian Meals", tamilName = "சிறப்பு தென்னிந்திய சாப்பாடு", price = 120.0, inStock = true))
                        database.productDao().insertProduct(ProductEntity(shopId = id.toInt(), name = "Ghee Roast Dosa", tamilName = "நெய் ரோஸ்ட் தோசை", price = 80.0, inStock = true))
                        database.productDao().insertProduct(ProductEntity(shopId = id.toInt(), name = "Mini Tiffin Combo", tamilName = "மினி டிபன் காம்போ", price = 100.0, inStock = true))
                    } else if (shop.name.contains("Rasi")) {
                        database.productDao().insertProduct(ProductEntity(shopId = id.toInt(), name = "Ponni Raw Rice 5Kg", tamilName = "பொன்னி பச்சரிசி 5 கிலோ", price = 320.0, inStock = true))
                        database.productDao().insertProduct(ProductEntity(shopId = id.toInt(), name = "Gold Winner Sunflower Oil 1L", tamilName = "கோல்டு வின்னர் சூரியகாந்தி எண்ணெய் 1லி", price = 145.0, inStock = true))
                        database.productDao().insertProduct(ProductEntity(shopId = id.toInt(), name = "Aashirvaad Shudh Chakki Atta 5Kg", tamilName = "ஆசிர்வாத் கோதுமை மாவு 5 கிலோ", price = 280.0, inStock = true))
                    } else if (shop.name.contains("Pharmacy")) {
                        database.productDao().insertProduct(ProductEntity(shopId = id.toInt(), name = "Multi-Vitamins Daily Tab (30)", tamilName = "தினசரி மல்டி-வைட்டமின் மாத்திரை (30)", price = 220.0, inStock = true))
                        database.productDao().insertProduct(ProductEntity(shopId = id.toInt(), name = "First Aid Medical Box Complete", tamilName = "முதலுதவி பாக்ஸ் முழுமையானது", price = 350.0, inStock = true))
                    }
                }

                // 2. Prepopulate News
                val news = listOf(
                    NewsEntity(
                        title = "Adikesava Perumal Chithirai festival commences with gold chariot procession",
                        tamilTitle = "ஆதிகேசவ பெருமாள் சித்திரை விழா தங்கத்தேர் ஊர்வலத்துடன் தொடங்கியது",
                        content = "The annual Chithirai Brahmotsavam of Sri Adikesava Perumal Temple in Sriperumbudur commenced this morning. Thousands of devotees from local areas and from Kancheepuram attended the gold chariot procession. Elaborate security arrangements have been established by local police.",
                        tamilContent = "ஸ்ரீபெரும்புதூர் ஸ்ரீ ஆதிகேசவ பெருமாள் கோவிலில் வருடாந்திர சித்திரை பிரம்மோற்சவம் இன்று காலை தொடங்கியது. உள்ளூர் மற்றும் காஞ்சிபுரத்தில் இருந்து ஆயிரக்கணக்கான பக்தர்கள் தங்கத்தேர் ஊர்வலத்தில் கலந்து கொண்டனர். உள்ளூர் போலீசார் விரிவான பாதுகாப்பு ஏற்பாடுகளை செய்துள்ளனர்.",
                        category = "General"
                    ),
                    NewsEntity(
                        title = "Chennai-Bengaluru Highway bypass traffic update",
                        tamilTitle = "சென்னை-பெங்களூரு நெடுஞ்சாலை பைபாஸ் போக்குவரத்து நிலவரம்",
                        content = "Heavy water pipeline laying work on bypass road has caused major speed limits near Sunguvarchatram junction. Heavy vehicles are advised to divert via Oragadam SIPCOT outer ring road to reach Outer Ring Road (Vandalur Bypass) to avoid peak delays.",
                        tamilContent = "பைபாஸ் சாலையில் குடிநீர் குழாய் பதிக்கும் பணி காரணமாக சுங்குவார்சத்திரம் சந்திப்பில் வேகம் குறைக்கப்பட்டுள்ளது. கனரக வாகனங்கள் நெரிசலை தவிர்க்க ஒரகடம் சிப்காட் வெளிவட்ட சாலை வழியாக செல்ல அறிவுறுத்தப்படுகிறார்கள்.",
                        category = "Traffic"
                    ),
                    NewsEntity(
                        title = "Sriperumbudur Municipality issues notices on dengue awareness and precautions",
                        tamilTitle = "ஸ்ரீபெரும்புதூர் நகராட்சி டெங்கு விழிப்புணர்வு மற்றும் முன்னெச்சரிக்கை அறிவிப்பு",
                        content = "The public authority has started special sanitary cleaning drives. Citizens are requested to empty water containers on Thuesdays and Saturdays to eliminate larvae breeding. Defaulters will be fined ₹500 according to municipal visual sanitary frameworks.",
                        tamilContent = "நகராட்சி நிர்வாகம் சார்பில் சிறப்பு சுகாதார துப்புரவு பணிகள் தொடங்கப்பட்டுள்ளன. லார்வாக்கள் உருவாவதை தவிர்க்க செவ்வாய் மற்றும் சனிக்கிழமைகளில் பொதுமக்கள் தண்ணீர் தொட்டிகளை சுத்தம் செய்ய வேண்டும். விதிகளை மீறுவோருக்கு ₹500 அபராதம் விதிக்கப்படும்.",
                        category = "Government"
                    )
                )
                for (item in news) {
                    database.newsDao().insertNews(item)
                }

                // 3. Prepopulate Jobs
                val jobs = listOf(
                    JobEntity(
                        companyName = "Foxconn Hon Hai Technology",
                        title = "Electronics Manufacturing Assembly Line Operator",
                        tamilTitle = "மின்னணுவியல் உற்பத்தி அசெம்பிளி லைன் ஆப்பரேட்டர்",
                        description = "Looking for active candidates for mobile casing assembly. Minimum qualification 12th Std pass or ITI Electronics. Free bus transport facility provided covering Sriperumbudur, Sunguvarchatram and Kancheepuram town.",
                        tamilDescription = "மொபைல் பகுதி அசெம்பிளிக்கு ஆட்கள் தேவை. குறைந்தபட்ச தகுதி 12வது அல்லது ஐடிஐ எலக்ட்ரானிக்ஸ் தேர்ச்சி இருக்க வேண்டும். ஸ்ரீபெரும்புதூர், சுங்குவார்சத்திரம் மற்றும் காஞ்சிபுரம் நகரை உள்ளடக்கிய இலவச பேருந்து வசதி வழங்கப்படுகிறது.",
                        jobType = "Factory",
                        location = "SIPCOT",
                        salaryRange = "₹16,500 - ₹21,000 / month",
                        contactPhone = "9840112244",
                        email = "careers.india@foxconn.com"
                    ),
                    JobEntity(
                        companyName = "Hotel Sri Krishna Vilas",
                        title = "Part-Time Food Delivery Executive",
                        tamilTitle = "பகுதி நேர உணவு விநியோகம் செய்பவர் (டெலிவரி பாய்)",
                        description = "Immediate vacancy for delivery boy with personal 2-wheeler and Android phone. Work hours 6PM to 10:30PM. Petrol allowance will be fully compensated.",
                        tamilDescription = "சொந்த இருசக்கர வாகனம் மற்றும் ஆண்ட்ராய்டு போன் உள்ள டெலிவரி பாய்க்கு உடனடி வேலைவாய்ப்பு. மாலை 6 மணி முதல் இரவு 10:30 மணி வரை. பெட்ரோல் அலவன்ஸ் முழுமையாக வழங்கப்படும்.",
                        jobType = "Part-Time",
                        location = "Town Center",
                        salaryRange = "₹7,500 + Incentives",
                        contactPhone = "9840212345",
                        email = "krishnavilas@gmail.com"
                    ),
                    JobEntity(
                        companyName = "Salcomp Manufacturing India Ltd",
                        title = "Production Trainee Engineer",
                        tamilTitle = "தயாரிப்பு பயிற்சி பொறியாளர்",
                        description = "Requirement of Diploma/BE Electronics and Electrical freshers (2025/2026 passed outs only). Shift duties with standard industrial benefits, canteen food, and medical insurance provided.",
                        tamilDescription = "டிப்ளமோ/பிஇ எலெக்ட்ரானிக்ஸ் மற்றும் எலக்ட்ரிக்கல் முடித்தவர்கள் தேவை (2025/2026ல் முடித்தவர்கள் மட்டும்). ஷிப்ட் முறையிலான பணி, கேண்டீன் உணவு மற்றும் மருத்துவ காப்பீட்டுடன் கூடிய வேலைவாய்ப்பு.",
                        jobType = "Full-Time",
                        location = "SIPCOT",
                        salaryRange = "₹18,000 - ₹24,000 / month",
                        contactPhone = "9940123987",
                        email = "hr.india@salcomp.com"
                    )
                )
                for (job in jobs) {
                    database.jobDao().insertJob(job)
                }

                // 4. Prepopulate Events
                val events = listOf(
                    EventEntity(
                        title = "Sri Ramanujachiyar Avatarotsavam Mahotsavam",
                        tamilTitle = "ஸ்ரீ ராமானுஜாச்சாரியார் அவதார மகோற்சவம்",
                        description = "Yearly celebrations marking Saint Sri Ramanuja's birth. Includes major cultural, spiritual lectures, vedic chanting and grand distribution of holy prasad.",
                        tamilDescription = "ஸ்ரீ ராமானுஜரின் அவதார தினத்தை முன்னிட்டு ஆண்டுதோறும் கொண்டாடப்படும் விழா. ஆன்மீக சொற்பொழிவுகள், வேத பாராயணம் மற்றும் பிரசாத விநியோகம் நடைபெறும்.",
                        date = "May 12, 2027",
                        time = "06:00 AM - 09:00 PM",
                        venue = "Sri Adikesava Perumal Temple, Sriperumbudur",
                        organizer = "Temple Trust & Devasthanam"
                    ),
                    EventEntity(
                        title = "Free General Medical & Eye Screening Camp",
                        tamilTitle = "இலவச பொது மருத்துவ மற்றும் கண் பரிசோதனை முகாம்",
                        description = "A localized civic community health outreach. Organised in partnership with Apollo Hospitals and local Lions Club. Spectacles distribution at subsidized costs and free blood checking details.",
                        tamilDescription = "பூங்கா ரோடில் உள்ள நகராட்சி அரங்கில் அப்போலோ மருத்துவமனை மற்றும் சிங்கம் சங்கம் சார்பில் நடைபெறும் இலவச கண் சிகிச்சை மற்றும் இரத்த அழுத்த பரிசோதனை முகாம்.",
                        date = "June 25, 2026",
                        time = "09:00 AM - 02:00 PM",
                        venue = "Municipal Community Building, Bus Stand Road",
                        organizer = "Lions Club Sriperumbudur Town"
                    )
                )
                for (evt in events) {
                    database.eventDao().insertEvent(evt)
                }
                
                // 5. Prepopulate initial reviews
                database.reviewDao().insertReview(ReviewEntity(shopId = 1, userName = "Preethi Ramanathan", rating = 5, comment = "The atmosphere of the temple and the holy pooja store feels pure. Highly recommended to buy prasad."))
                database.reviewDao().insertReview(ReviewEntity(shopId = 2, userName = "Karthik Raja", rating = 4, comment = "Excellent Ghee Roast Dosa and sambar! The crowd is high on Sunday mornings but service is fast."))
            }
        }
    }
}

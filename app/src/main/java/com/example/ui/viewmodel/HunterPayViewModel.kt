package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.DatabaseProvider
import com.example.data.model.UserProfile
import com.example.data.model.Transaction
import com.example.data.model.TravelBooking
import com.example.data.api.GeminiClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

// Flight Schedule Form State
data class FlightOption(
    val id: String,
    val airline: String,
    val code: String,
    val departureTime: String,
    val arrivalTime: String,
    val price: Double
)

// Hotel Option Form State
data class HotelOption(
    val id: String,
    val name: String,
    val rating: Int,
    val pricePerNight: Double,
    val location: String,
    val amenities: List<String>
)

// Train Option Form State
data class TrainOption(
    val id: String,
    val name: String,
    val carriageClass: String,
    val departureTime: String,
    val arrivalTime: String,
    val price: Double
)

class HunterPayViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DatabaseProvider.getRepository(application)

    // Main States from Room
    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val transactions: StateFlow<List<Transaction>> = repository.transactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookings: StateFlow<List<TravelBooking>> = repository.bookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Feedback Message (snackbars)
    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    // --- Search & Mock Data States ---
    
    // Flight Searches
    val flightOrigins = listOf("Jakarta (CGK)", "Bali (DPS)", "Surabaya (SUB)", "Yogyakarta (YIA)", "Singapore (SIN)")
    val flightDestinations = listOf("Bali (DPS)", "Jakarta (CGK)", "Singapore (SIN)", "Yogyakarta (YIA)", "Medan (KNO)")
    
    private val _searchFlightOrigin = MutableStateFlow("Jakarta (CGK)")
    val searchFlightOrigin = _searchFlightOrigin.asStateFlow()

    private val _searchFlightDest = MutableStateFlow("Bali (DPS)")
    val searchFlightDest = _searchFlightDest.asStateFlow()

    private val _searchFlightDate = MutableStateFlow("25 Juni 2026")
    val searchFlightDate = _searchFlightDate.asStateFlow()

    private val _searchFlightCabin = MutableStateFlow("Ekonomi")
    val searchFlightCabin = _searchFlightCabin.asStateFlow()

    private val _flightResults = MutableStateFlow<List<FlightOption>>(emptyList())
    val flightResults = _flightResults.asStateFlow()

    // Hotel Searches
    val hotelCities = listOf("Bali", "Jakarta", "Yogyakarta", "Bandung", "Surabaya")
    
    private val _searchHotelCity = MutableStateFlow("Bali")
    val searchHotelCity = _searchHotelCity.asStateFlow()

    private val _searchHotelCheckIn = MutableStateFlow("28 Juni 2026")
    val searchHotelCheckIn = _searchHotelCheckIn.asStateFlow()

    private val _searchHotelNights = MutableStateFlow(2)
    val searchHotelNights = _searchHotelNights.asStateFlow()

    private val _hotelResults = MutableStateFlow<List<HotelOption>>(emptyList())
    val hotelResults = _hotelResults.asStateFlow()

    // Train Searches
    val trainStations = listOf("Jakarta Gambir (GMR)", "Yogyakarta (YK)", "Surabaya Gubeng (SGU)", "Bandung (BD)")
    
    private val _searchTrainOrigin = MutableStateFlow("Jakarta Gambir (GMR)")
    val searchTrainOrigin = _searchTrainOrigin.asStateFlow()

    private val _searchTrainDest = MutableStateFlow("Yogyakarta (YK)")
    val searchTrainDest = _searchTrainDest.asStateFlow()

    private val _searchTrainDate = MutableStateFlow("26 Juni 2026")
    val searchTrainDate = _searchTrainDate.asStateFlow()

    private val _trainResults = MutableStateFlow<List<TrainOption>>(emptyList())
    val trainResults = _trainResults.asStateFlow()

    // --- Pulsa & Paket Data State ---
    private val _pulsaPhone = MutableStateFlow("")
    val pulsaPhone = _pulsaPhone.asStateFlow()

    val pulsaProvider: StateFlow<String> = _pulsaPhone
        .map { phone ->
            when {
                phone.length < 4 -> ""
                phone.startsWith("0811") || phone.startsWith("0812") || phone.startsWith("0813") || phone.startsWith("0821") || phone.startsWith("0822") || phone.startsWith("0852") || phone.startsWith("0853") -> "TELKOMSEL"
                phone.startsWith("0814") || phone.startsWith("0815") || phone.startsWith("0816") || phone.startsWith("0855") || phone.startsWith("0856") || phone.startsWith("0857") || phone.startsWith("0858") -> "INDOSAT"
                phone.startsWith("0817") || phone.startsWith("0818") || phone.startsWith("0819") || phone.startsWith("0859") || phone.startsWith("0877") || phone.startsWith("0878") -> "XL AXIATA"
                phone.startsWith("0895") || phone.startsWith("0896") || phone.startsWith("0897") || phone.startsWith("0898") || phone.startsWith("0899") -> "TRI (3)"
                else -> "OTHER"
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val pulsaPackages = listOf(
        Pair("Pulsa Rp 10.000", 11500.0),
        Pair("Pulsa Rp 25.000", 26000.0),
        Pair("Pulsa Rp 50.000", 51000.0),
        Pair("Pulsa Rp 100.000", 101000.0),
        Pair("Paket Internet 15GB (Bulanan)", 68000.0),
        Pair("Paket Internet 30GB (Bulanan)", 115000.0),
        Pair("Paket Internet Unlimited Chat (Mingguan)", 22000.0)
    )

    // --- PLN State ---
    private val _plnCustId = MutableStateFlow("")
    val plnCustId = _plnCustId.asStateFlow()

    private val _plnSelectedAmount = MutableStateFlow(50000.0)
    val plnSelectedAmount = _plnSelectedAmount.asStateFlow()

    val plnOptions = listOf(20000.0, 50000.0, 100000.0, 200000.0, 500000.0)

    // --- BPJS State ---
    private val _bpjsMemberId = MutableStateFlow("")
    val bpjsMemberId = _bpjsMemberId.asStateFlow()

    private val _bpjsPeriods = MutableStateFlow(1) // months
    val bpjsPeriods = _bpjsPeriods.asStateFlow()

    val bpjsRatePerMonth = 150000.0 // Kelas 1 Premium

    // --- Chat Room (HunterPAI Assistant) ---
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage("Halo! Saya **HunterPAI**, Asisten AI Pintar dari HunterPay. 🌴\n\nAda yang bisa saya bantu untuk merencanakan liburan Anda hari ini? Tanyakan destinasi menarik di Bali, Jogja, atau hotel terbaik dengan budget Anda!", false)
    ))
    val chatMessages = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading = _isChatLoading.asStateFlow()


    init {
        // Hydrate initial mock search arrays
        searchFlights()
        searchHotels()
        searchTrains()
    }

    // --- Search triggers ---
    
    fun setFlightOrigin(valStr: String) { _searchFlightOrigin.value = valStr }
    fun setFlightDest(valStr: String) { _searchFlightDest.value = valStr }
    fun setFlightDate(valStr: String) { _searchFlightDate.value = valStr }
    fun setFlightCabin(valStr: String) { _searchFlightCabin.value = valStr }

    fun searchFlights() {
        val o = _searchFlightOrigin.value
        val d = _searchFlightDest.value
        _flightResults.value = listOf(
            FlightOption("FL-001", "Garuda Indonesia", "GA-402", "08:15", "10:10", 1250000.0),
            FlightOption("FL-002", "Batik Air", "ID-6052", "10:30", "12:20", 850000.0),
            FlightOption("FL-003", "Citilink", "QG-231", "14:15", "16:05", 680000.0),
            FlightOption("FL-004", "Super Air Jet", "IU-732", "16:45", "18:40", 590000.0),
            FlightOption("FL-005", "Lion Air", "JT-102", "19:00", "20:55", 520000.0)
        )
    }

    fun setHotelCity(city: String) { _searchHotelCity.value = city }
    fun setHotelCheckIn(date: String) { _searchHotelCheckIn.value = date }
    fun setHotelNights(nights: Int) { _searchHotelNights.value = nights }

    fun searchHotels() {
        val city = _searchHotelCity.value
        _hotelResults.value = when (city) {
            "Bali" -> listOf(
                HotelOption("HT-001", "The Apurva Kempinski Bali", 5, 2350000.0, "Nusa Dua, Bali", listOf("Kolam Renang", "Spa", "Akses Pantai", "Sarapan")),
                HotelOption("HT-002", "Hard Rock Hotel Bali", 4, 1400000.0, "Kuta, Bali", listOf("Air Terjun Air", "Karaoke", "Kids Club", "Gym")),
                HotelOption("HT-003", "Kuta Paradiso Resort", 4, 850000.0, "Kuta, Bali", listOf("Pusat Kebugaran", "Bar", "Sarapan Gratis")),
                HotelOption("HT-004", "Alam Ubud Culture Villa", 5, 1850000.0, "Ubud, Bali", listOf("Infinity Pool", "Yoga Area", "Taman Tropis"))
            )
            "Jakarta" -> listOf(
                HotelOption("HT-101", "Hotel Indonesia Kempinski", 5, 1950000.0, "Bundaran HI, Jakarta", listOf("Mal Access", "Kolam Renang", "Luxury Lounge")),
                HotelOption("HT-102", "The Ritz-Carlton Mega Kuningan", 5, 2150000.0, "Mega Kuningan, Jakarta", listOf("Pijat", "Outdoor Dining", "Meeting Room")),
                HotelOption("HT-103", "Neo Hotel Tendean", 3, 480000.0, "Mampang, Jakarta", listOf("WiFi Super Cepat", "Kopi Sepuasnya", "Shower Hangat"))
            )
            "Yogyakarta" -> listOf(
                HotelOption("HT-201", "Grand Hyatt Yogyakarta", 5, 1100000.0, "Sleman, Yogyakarta", listOf("Lapangan Golf", "Kolam Renang Batu", "Restoran")),
                HotelOption("HT-202", "Tentrem Hotel Yogyakarta", 5, 1350000.0, "Jetis, Yogyakarta", listOf("Spa Herbal", "Gym", "Sarapan Gudeg Premium")),
                HotelOption("HT-203", "YATS Colony", 4, 620000.0, "Wirobrajan, Yogyakarta", listOf("Desain Artistik", "Kafe Organik", "Bulan Madu Suite"))
            )
            else -> listOf(
                HotelOption("HT-MOK", "$city Premium Resort & Inn", 4, 750000.0, "$city Center", listOf("Sarapan Gratis", "WiFi", "AC", "Laundry"))
            )
        }
    }

    fun setTrainOrigin(orig: String) { _searchTrainOrigin.value = orig }
    fun setTrainDest(dest: String) { _searchTrainDest.value = dest }
    fun setTrainDate(date: String) { _searchTrainDate.value = date }

    fun searchTrains() {
        _trainResults.value = listOf(
            TrainOption("TR-001", "Argo Bromo Anggrek", "Eksekutif", "08:00", "16:30", 480000.0),
            TrainOption("TR-002", "Taksaka", "Eksekutif Luxury", "09:15", "15:20", 650000.0),
            TrainOption("TR-003", "Senja Utama", "Ekonomi Premium", "19:00", "01:25", 280000.0),
            TrainOption("TR-004", "Fajar Utama", "Bisnis", "06:15", "12:45", 350000.0)
        )
    }

    // --- Inputs Mutators ---
    
    fun updatePulsaPhone(num: String) {
        _pulsaPhone.value = num
    }

    fun updatePlnCustId(id: String) {
        _plnCustId.value = id
    }

    fun selectPlnAmount(amt: Double) {
        _plnSelectedAmount.value = amt
    }

    fun updateBpjsId(id: String) {
        _bpjsMemberId.value = id
    }

    fun selectBpjsPeriods(m: Int) {
        _bpjsPeriods.value = m
    }

    // --- Transaction Handlers ---

    // Top up simulation
    fun topupHunterPay(amount: Double, bankName: String) {
        viewModelScope.launch {
            val refNo = "TOP-${System.currentTimeMillis() % 10000000}"
            repository.topUpBalance(
                amount = amount,
                refNo = refNo,
                description = "Top up dari virtual account $bankName"
            )
            _uiEvent.emit("Isi saldo Rp ${formatRupiah(amount)} berhasil via $bankName!")
        }
    }

    // Ticket Booking Simulation
    fun bookFlightTicket(flight: FlightOption, passengerName: String, onCompletion: (Boolean) -> Unit) {
        viewModelScope.launch {
            val profile = repository.getUserProfileSync()
            if (profile == null) {
                _uiEvent.emit("Profil belum siap.")
                onCompletion(false)
                return@launch
            }
            if (profile.balance < flight.price) {
                _uiEvent.emit("Saldo HunterPay Cash tidak mencukupi. Silakan isi saldo terlebih dahulu.")
                onCompletion(false)
                return@launch
            }

            val bookingRef = "HPF-${UUID.randomUUID().toString().take(6).uppercase()}"
            val booking = TravelBooking(
                type = "FLIGHT",
                title = "${_searchFlightOrigin.value} ke ${_searchFlightDest.value}",
                detailInfo = "Maskapai: ${flight.airline} (${flight.code})\nJadwal: ${_searchFlightDate.value} | ${flight.departureTime} - ${flight.arrivalTime}\nKelas: ${_searchFlightCabin.value}",
                datetime = "${_searchFlightDate.value}, ${flight.departureTime}",
                paxName = passengerName,
                amountPaid = flight.price,
                bookingRef = bookingRef,
                status = "UPCOMING"
            )

            val success = repository.makePaymentAndBook(
                amount = flight.price,
                txType = "FLIGHT",
                description = "Pembelian Tiket ${flight.airline} ke ${_searchFlightDest.value}",
                refNo = bookingRef,
                booking = booking
            )

            if (success) {
                _uiEvent.emit("Tiket penerbangan ${flight.airline} berhasil diterbitkan!")
                onCompletion(true)
            } else {
                _uiEvent.emit("Transaksi gagal diproses.")
                onCompletion(false)
            }
        }
    }

    fun bookHotelRoom(hotel: HotelOption, days: Int, guestName: String, onCompletion: (Boolean) -> Unit) {
        viewModelScope.launch {
            val profile = repository.getUserProfileSync()
            if (profile == null) {
                _uiEvent.emit("Profil belum siap.")
                onCompletion(false)
                return@launch
            }
            val totalPrice = hotel.pricePerNight * days
            if (profile.balance < totalPrice) {
                _uiEvent.emit("Saldo HunterPay Cash tidak mencukupi untuk booking hotel.")
                onCompletion(false)
                return@launch
            }

            val bookingRef = "HPH-${UUID.randomUUID().toString().take(6).uppercase()}"
            val booking = TravelBooking(
                type = "HOTEL",
                title = hotel.name,
                detailInfo = "Alamat: ${hotel.location}\nDurasi: $days malam (Check-In: ${_searchHotelCheckIn.value})\nFasilitas: ${hotel.amenities.joinToString(", ")}",
                datetime = "C/I: ${_searchHotelCheckIn.value} ($days malam)",
                paxName = guestName,
                amountPaid = totalPrice,
                bookingRef = bookingRef,
                status = "UPCOMING"
            )

            val success = repository.makePaymentAndBook(
                amount = totalPrice,
                txType = "HOTEL",
                description = "Booking Hotel: ${hotel.name} ($days malam)",
                refNo = bookingRef,
                booking = booking
            )

            if (success) {
                _uiEvent.emit("Voucher Hotel ${hotel.name} berhasil diterbitkan!")
                onCompletion(true)
            } else {
                _uiEvent.emit("Transaksi gagal diproses.")
                onCompletion(false)
            }
        }
    }

    fun bookTrainTicket(train: TrainOption, paxName: String, onCompletion: (Boolean) -> Unit) {
        viewModelScope.launch {
            val profile = repository.getUserProfileSync()
            if (profile == null) {
                _uiEvent.emit("Profil belum siap.")
                onCompletion(false)
                return@launch
            }
            if (profile.balance < train.price) {
                _uiEvent.emit("Saldo tidak mencukupi untuk tiket kereta.")
                onCompletion(false)
                return@launch
            }

            val bookingRef = "HPT-${UUID.randomUUID().toString().take(6).uppercase()}"
            val booking = TravelBooking(
                type = "TRAIN",
                title = "${_searchTrainOrigin.value} ke ${_searchTrainDest.value}",
                detailInfo = "Kereta: ${train.name}\nJadwal: ${_searchTrainDate.value} | ${train.departureTime} - ${train.arrivalTime}\nKelas: ${train.carriageClass}",
                datetime = "${_searchTrainDate.value}, ${train.departureTime}",
                paxName = paxName,
                amountPaid = train.price,
                bookingRef = bookingRef,
                status = "UPCOMING"
            )

            val success = repository.makePaymentAndBook(
                amount = train.price,
                txType = "TRAIN",
                description = "Tiket Kereta ${train.name} ke ${_searchTrainDest.value}",
                refNo = bookingRef,
                booking = booking
            )

            if (success) {
                _uiEvent.emit("E-Tiket Kereta ${train.name} berhasil diterbitkan!")
                onCompletion(true)
            } else {
                _uiEvent.emit("Transaksi gagal diproses.")
                onCompletion(false)
            }
        }
    }

    // Bill utilities checkout
    fun payMobileBill(amount: Double, denomName: String, onCompletion: (Boolean) -> Unit) {
        viewModelScope.launch {
            val provider = pulsaProvider.value
            val phone = _pulsaPhone.value
            if (phone.isEmpty() || phone.length < 9) {
                _uiEvent.emit("Format nomor telepon salah.")
                onCompletion(false)
                return@launch
            }

            val success = repository.makePayment(
                amount = amount,
                txType = "PULSA",
                description = "Pembelian $denomName untuk $phone ($provider)",
                refNo = "PLS-${System.currentTimeMillis() % 10000000}"
            )

            if (success) {
                _uiEvent.emit("Transaksi $denomName ke $phone berhasil!")
                onCompletion(true)
            } else {
                _uiEvent.emit("Saldo HunterPay Cash tidak cukup untuk membeli pulsa.")
                onCompletion(false)
            }
        }
    }

    fun payPlnBill(onCompletion: (Boolean) -> Unit) {
        viewModelScope.launch {
            val custId = _plnCustId.value
            val amount = _plnSelectedAmount.value
            if (custId.isEmpty() || custId.length < 8) {
                _uiEvent.emit("Format ID Pelanggan PLN salah.")
                onCompletion(false)
                return@launch
            }

            val success = repository.makePayment(
                amount = amount,
                txType = "PLN",
                description = "Token Listrik PLN - ID Pel: $custId",
                refNo = "PLN-${System.currentTimeMillis() % 10000000}"
            )

            if (success) {
                _uiEvent.emit("Pembelian Token Listrik Rp ${formatRupiah(amount)} sukses!")
                // Also add a system info alert
                onCompletion(true)
            } else {
                _uiEvent.emit("Saldo tidak cukup untuk membeli Token PLN.")
                onCompletion(false)
            }
        }
    }

    fun payBpjsBill(onCompletion: (Boolean) -> Unit) {
        viewModelScope.launch {
            val memberId = _bpjsMemberId.value
            val months = _bpjsPeriods.value
            if (memberId.isEmpty() || memberId.length < 8) {
                _uiEvent.emit("Format No BPJS salah.")
                onCompletion(false)
                return@launch
            }

            val billAmount = bpjsRatePerMonth * months
            val success = repository.makePayment(
                amount = billAmount,
                txType = "BPJS",
                description = "Tagihan BPJS Kesehatan - No: $memberId ($months Bulan)",
                refNo = "BPJ-${System.currentTimeMillis() % 10000000}"
            )

            if (success) {
                _uiEvent.emit("Pembayaran BPJS Kesehatan $months Bulan berhasil!")
                onCompletion(true)
            } else {
                _uiEvent.emit("Saldo Anda tidak cukup untuk membayar tagihan BPJS.")
                onCompletion(false)
            }
        }
    }

    // --- Gemini Chat Trigger ---
    fun sendChatMessage(msg: String) {
        if (msg.trim().isEmpty()) return
        
        // Append user response
        val userItem = ChatMessage(msg, true)
        _chatMessages.value = _chatMessages.value + userItem
        
        _isChatLoading.value = true
        
        viewModelScope.launch {
            // Plan trip with Gemini
            val replyText = GeminiClient.planTrip(msg)
            
            // Append AI response
            _chatMessages.value = _chatMessages.value + ChatMessage(replyText, false)
            _isChatLoading.value = false
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage("Halo! Saya **HunterPAI**, Asisten AI Pintar dari HunterPay. 🌴\n\nAda yang bisa saya bantu untuk merencanakan liburan Anda hari ini? Tanyakan destinasi menarik di Bali, Jogja, atau hotel terbaik dengan budget Anda!", false)
        )
    }

    // Helper formatter
    fun formatRupiah(amount: Double): String {
        return try {
            val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            formatter.maximumFractionDigits = 0
            // format returns "Rp" or custom currency sign.
            var res = formatter.format(amount)
            if (res.startsWith("Rp")) {
                res = res.replace("Rp", "Rp ")
            }
            res
        } catch (e: Exception) {
            "Rp " + String.format("%,.0f", amount)
        }
    }

    fun postUiNotification(msg: String) {
        viewModelScope.launch {
            _uiEvent.emit(msg)
        }
    }
}

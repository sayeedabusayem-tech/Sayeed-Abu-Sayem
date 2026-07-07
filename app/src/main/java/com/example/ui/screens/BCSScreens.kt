package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.BCSViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(viewModel: BCSViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isBangla by viewModel.isBangla.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    MyApplicationTheme(darkTheme = isDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (currentScreen) {
                "landing" -> LandingScreen(viewModel)
                "login" -> LoginScreen(viewModel)
                "register" -> RegisterScreen(viewModel)
                else -> AuthenticatedScaffold(viewModel, currentScreen)
            }
        }
    }
}

// 1. Landing Screen
@Composable
fun LandingScreen(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // App logo & Name
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "BCS Companion Logo",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(2.dp, SaffronGold, RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isBangla) "বিসিএস কম্প্যানিয়ন" else "BCS Companion",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = EmeraldGreen,
            textAlign = TextAlign.Center
        )

        Text(
            text = if (isBangla) "আপনার ক্যাডার হওয়ার বিশ্বস্ত সহযোগী" else "Your trusted partner for BCS & Govt jobs",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Hero Image
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bcs_companion_hero),
                contentDescription = "BCS Hero Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Countdown Timer Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SaffronOrange.copy(alpha = 0.1f)),
            border = BorderStroke(1.dp, SaffronOrange)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isBangla) "⏳ ৪৬তম বিসিএস প্রিলিমিনারি কাউন্টডাউন" else "⏳ 46th BCS Prelims Countdown",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SaffronOrange
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CountdownUnit(value = "24", label = if (isBangla) "দিন" else "Days")
                    CountdownUnit(value = "11", label = if (isBangla) "ঘণ্টা" else "Hours")
                    CountdownUnit(value = "45", label = if (isBangla) "মিনিট" else "Mins")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Features Checklist
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FeatureHighlightItem(
                icon = Icons.Default.CheckCircle,
                title = if (isBangla) "পূর্ণাঙ্গ স্টাডি প্ল্যানার ও রুটিন" else "Full Study Planner & Routines"
            )
            FeatureHighlightItem(
                icon = Icons.Default.Quiz,
                title = if (isBangla) "বিষয়ভিত্তিক এবং টপিকভিত্তিক প্রিলিমিনারি MCQ" else "Subject-wise & Topic-wise MCQs"
            )
            FeatureHighlightItem(
                icon = Icons.Default.Psychology,
                title = if (isBangla) "এআই মেন্টর চ্যাট সাপোর্ট (Gemini Powered)" else "AI Mentor Chat Support"
            )
            FeatureHighlightItem(
                icon = Icons.Default.Analytics,
                title = if (isBangla) "অগ্রগতি বিশ্লেষণ ও মক টেস্ট ট্র্যাকার" else "Analytics & Mock Test Trackers"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Primary Action Button
        Button(
            onClick = { viewModel.setScreen("register") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("register_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (isBangla) "অ্যাকাউন্ট তৈরি করুন" else "Create Account",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { viewModel.setScreen("login") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("login_btn"),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldGreen),
            border = BorderStroke(1.5.dp, EmeraldGreen),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (isBangla) "লগইন করুন" else "Sign In",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enter as Guest option
        TextButton(
            onClick = { viewModel.login("BCS Guest", "12345") },
            modifier = Modifier.testTag("guest_btn")
        ) {
            Text(
                text = if (isBangla) "গেস্ট হিসেবে প্রবেশ করুন" else "Enter as Guest",
                color = SaffronOrange,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun CountdownUnit(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = EmeraldGreen
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun FeatureHighlightItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EmeraldGreen,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


// 2. Login Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Lock",
            tint = EmeraldGreen,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isBangla) "লগইন করুন" else "Welcome Back",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = EmeraldGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isBangla) "নিচের তথ্যাদি দিয়ে অ্যাকাউন্টটি লগইন করুন" else "Login using your username & password",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(if (isBangla) "ব্যবহারকারীর নাম" else "Username") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_username"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(if (isBangla) "পাসওয়ার্ড" else "Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_password"),
            singleLine = true
        )

        if (errorMsg != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMsg!!, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    errorMsg = if (isBangla) "সব ঘর পূরণ করুন" else "All fields are required"
                } else {
                    viewModel.login(username, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("login_submit"),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
        ) {
            Text(if (isBangla) "লগইন" else "Login", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { viewModel.setScreen("register") }) {
            Text(
                text = if (isBangla) "নতুন অ্যাকাউন্ট তৈরি করতে চান? রেজিস্টার করুন" else "Don't have an account? Register",
                color = SaffronOrange,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(onClick = {
            errorMsg = if (isBangla) "পাসওয়ার্ড রিসেট লিংক আপনার ইমেইলে পাঠানো হয়েছে (সিমুলেশন)" else "Password reset link sent (Simulation)"
        }) {
            Text(
                text = if (isBangla) "পাসওয়ার্ড ভুলে গেছেন?" else "Forgot Password?",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { viewModel.setScreen("landing") }) {
            Text(if (isBangla) "← পিছনে যান" else "← Back to landing", color = EmeraldGreen)
        }
    }
}


// 3. Register Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var targetCadre by remember { mutableStateOf("BCS Admin") }
    var password by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AppRegistration,
            contentDescription = "Reg",
            tint = EmeraldGreen,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isBangla) "রেজিস্টার করুন" else "Join BCS Companion",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = EmeraldGreen
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(if (isBangla) "ব্যবহারকারীর নাম" else "Username") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().testTag("reg_username"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(if (isBangla) "ইমেইল অ্যাড্রেস" else "Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().testTag("reg_email"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = targetCadre,
            onValueChange = { targetCadre = it },
            label = { Text(if (isBangla) "টার্গেট বিসিএস ক্যাডার" else "Target Cadre Choice") },
            leadingIcon = { Icon(Icons.Default.Stars, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().testTag("reg_cadre"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(if (isBangla) "পাসওয়ার্ড" else "Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().testTag("reg_password"),
            singleLine = true
        )

        if (errorMsg != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMsg!!, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (username.isBlank() || email.isBlank() || password.isBlank()) {
                    errorMsg = if (isBangla) "সব ঘর পূরণ করুন" else "All fields are required"
                } else if (!email.contains("@")) {
                    errorMsg = if (isBangla) "বৈধ ইমেইল দিন" else "Please enter a valid email"
                } else {
                    viewModel.register(username, email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("reg_submit"),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
        ) {
            Text(if (isBangla) "নিবন্ধন করুন" else "Sign Up", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { viewModel.setScreen("login") }) {
            Text(
                text = if (isBangla) "ইতিমধ্যেই অ্যাকাউন্ট আছে? লগইন করুন" else "Already have an account? Sign In",
                color = SaffronOrange,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { viewModel.setScreen("landing") }) {
            Text(if (isBangla) "← পিছনে যান" else "← Back to landing", color = EmeraldGreen)
        }
    }
}


// --- 4. Main Scaffold & Navigation Hub ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticatedScaffold(viewModel: BCSViewModel, currentScreen: String) {
    val isBangla by viewModel.isBangla.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    // Map screens to our 5 Primary Hubs for navigation
    val selectedTab = when (currentScreen) {
        "dashboard", "admin" -> "dashboard"
        "study_planner", "subjects", "notes", "resources" -> "study"
        "mcq", "mock_test" -> "practice"
        "ai_mentor", "affairs" -> "mentor"
        "profile", "settings" -> "profile"
        else -> "dashboard"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isBangla) "বিসিএস কম্প্যানিয়ন" else "BCS Companion",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleLanguage() }) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Switch Language",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { viewModel.toggleDarkMode() }) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { viewModel.logout() }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EmeraldGreen)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == "dashboard",
                    onClick = { viewModel.setScreen("dashboard") },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text(if (isBangla) "ড্যাশবোর্ড" else "Dashboard", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = selectedTab == "study",
                    onClick = { viewModel.setScreen("subjects") },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "Study") },
                    label = { Text(if (isBangla) "পড়াশোনা" else "Study", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = selectedTab == "practice",
                    onClick = { viewModel.setScreen("mcq") },
                    icon = { Icon(Icons.Default.Quiz, contentDescription = "Practice") },
                    label = { Text(if (isBangla) "অনুশীলন" else "Practice", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = selectedTab == "mentor",
                    onClick = { viewModel.setScreen("ai_mentor") },
                    icon = { Icon(Icons.Default.Psychology, contentDescription = "AI Mentor") },
                    label = { Text(if (isBangla) "এআই মেন্টর" else "AI Mentor", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = selectedTab == "profile",
                    onClick = { viewModel.setScreen("profile") },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
                    label = { Text(if (isBangla) "প্রোফাইল" else "Profile", fontSize = 11.sp) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                "dashboard" -> DashboardHub(viewModel)
                "admin" -> AdminPanelScreen(viewModel)
                // Study subpages
                "subjects" -> StudyHub(viewModel, 0)
                "study_planner" -> StudyHub(viewModel, 1)
                "notes" -> StudyHub(viewModel, 2)
                "resources" -> StudyHub(viewModel, 3)
                // Practice subpages
                "mcq" -> PracticeHub(viewModel, 0)
                "mock_test" -> PracticeHub(viewModel, 1)
                // Mentor / affairs subpages
                "ai_mentor" -> MentorHub(viewModel, 0)
                "affairs" -> MentorHub(viewModel, 1)
                // Profile & settings subpages
                "profile" -> ProfileHub(viewModel, 0)
                "settings" -> ProfileHub(viewModel, 1)
            }
        }
    }
}

// 5. Dashboard Hub Screen
@Composable
fun DashboardHub(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val streak by viewModel.studyStreak.collectAsState()
    val hoursToday by viewModel.dailyHoursToday.collectAsState()
    val pomodoroMode by viewModel.pomodoroMode.collectAsState()
    val pomodoroTimeLeft by viewModel.pomodoroSecondsLeft.collectAsState()
    val pomodoroRunning by viewModel.pomodoroRunning.collectAsState()
    val tasksList by viewModel.tasks.collectAsState()
    val announcements by viewModel.announcements.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcoming header with streak badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isBangla) "শুভ দিন, $currentUser!" else "Hello, $currentUser!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen
                )
                Text(
                    text = if (isBangla) "আজকের বিসিএস চ্যালেঞ্জ সম্পন্ন করুন" else "Achieve your BCS goals today",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }

            // Streak Badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(SaffronOrange.copy(alpha = 0.15f))
                    .border(1.dp, SaffronOrange, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🔥", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isBangla) "$streak দিন" else "$streak Days",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = SaffronOrange
                )
            }
        }

        // Daily tracker card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isBangla) "⏱️ দৈনিক পড়ার লক্ষমাত্রা" else "⏱️ Daily Study Target",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isBangla) "$hoursToday ঘণ্টা / ৮ ঘণ্টা" else "$hoursToday hr / 8 hr",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreen
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = (hoursToday / 8f).coerceIn(0f, 1f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = EmeraldGreen,
                    trackColor = EmeraldGreen.copy(alpha = 0.2f)
                )
            }
        }

        // Pomodoro Timer Widget
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isBangla) "🍅 পোমোডোরো স্টাডি টাইমার" else "🍅 Pomodoro Study Timer",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (pomodoroMode == "Study") (if (isBangla) "পড়ার সেশন" else "Study Session") else (if (isBangla) "বিরতি" else "Break"),
                    style = MaterialTheme.typography.bodySmall,
                    color = SaffronOrange,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Time Left display
                val mins = pomodoroTimeLeft / 60
                val secs = pomodoroTimeLeft % 60
                val formattedTime = String.format("%02d:%02d", mins, secs)

                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { viewModel.togglePomodoro() },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                    ) {
                        Icon(
                            imageVector = if (pomodoroRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Start Pause"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            if (pomodoroRunning) {
                                if (isBangla) "বিরতি নিন" else "Pause"
                            } else {
                                if (isBangla) "শুরু করুন" else "Start"
                            }
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.resetPomodoro() },
                        border = BorderStroke(1.dp, SaffronOrange)
                    ) {
                        Text(if (isBangla) "রিসেট" else "Reset", color = SaffronOrange)
                    }
                }
            }
        }

        // Announcement Board
        if (announcements.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SaffronGold.copy(alpha = 0.1f)),
                border = BorderStroke(1.dp, SaffronGold)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Campaign, contentDescription = "Announcement", tint = SaffronOrange)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBangla) "📢 নোটিশ ও ঘোষণা" else "📢 Notices & Announcements",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SaffronOrange
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = announcements.first(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // Today's Goals checklist summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBangla) "🎯 আজকের গোল তালিকা" else "🎯 Today's Task Goals",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { viewModel.setScreen("study_planner") }) {
                        Text(if (isBangla) "সব দেখুন" else "View All", color = EmeraldGreen)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val dailyTasks = tasksList.filter { it.category == "Daily" }.take(3)
                if (dailyTasks.isEmpty()) {
                    Text(
                        text = if (isBangla) "কোনো টাস্ক নেই! নতুন টাস্ক যোগ করুন।" else "No goals set for today!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        dailyTasks.forEach { task ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.toggleTaskCompletion(task) }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = task.isCompleted,
                                    onCheckedChange = { viewModel.toggleTaskCompletion(task) },
                                    colors = CheckboxDefaults.colors(checkedColor = EmeraldGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = task.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (task.isCompleted) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Daily MCQ Question card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBangla) "💡 আজকের কুইজ চ্যালেঞ্জ" else "💡 Daily MCQ Challenge",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isBangla) "বাংলা ব্যাকরণ: 'বাংলা ভাষায় যৌগিক স্বরধ্বনি কয়টি?'" else "Bangla Grammar: 'How many diphthongs in Bangla?'",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { viewModel.setScreen("mcq") },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronOrange)
                ) {
                    Text(if (isBangla) "কুইজে অংশ নিন" else "Solve Challenge")
                }
            }
        }

        // Admin panel quick link at bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            OutlinedButton(
                onClick = { viewModel.setScreen("admin") },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f))
            ) {
                Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isBangla) "অ্যাডমিন মডারেটর প্যানেল" else "Admin Moderator Panel", fontSize = 12.sp)
            }
        }
    }
}


// 6. Study Hub Module (Contains: Subjects, Planner, Notes, Resources tabs)
@Composable
fun StudyHub(viewModel: BCSViewModel, defaultTab: Int) {
    val isBangla by viewModel.isBangla.collectAsState()
    var selectedSubTab by remember { mutableStateOf(defaultTab) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = EmeraldGreen,
            contentColor = Color.White
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = { Text(if (isBangla) "বিষয়সমূহ" else "Subjects", fontSize = 12.sp) }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text(if (isBangla) "প্ল্যানার" else "Planner", fontSize = 12.sp) }
            )
            Tab(
                selected = selectedSubTab == 2,
                onClick = { selectedSubTab = 2 },
                text = { Text(if (isBangla) "নোটবুক" else "Notebook", fontSize = 12.sp) }
            )
            Tab(
                selected = selectedSubTab == 3,
                onClick = { selectedSubTab = 3 },
                text = { Text(if (isBangla) "রিসোর্স" else "Resources", fontSize = 12.sp) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedSubTab) {
                0 -> SubjectsTab(viewModel)
                1 -> PlannerTab(viewModel)
                2 -> NotebookTab(viewModel)
                3 -> ResourcesTab(viewModel)
            }
        }
    }
}

@Composable
fun SubjectsTab(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val progressList by viewModel.subjectProgressList.collectAsState()
    var selectedSubjectDetails by remember { mutableStateOf<SubjectProgress?>(null) }

    if (selectedSubjectDetails != null) {
        // Detailed popup card for subject
        SubjectDetailSheet(selectedSubjectDetails!!, viewModel) {
            selectedSubjectDetails = null
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(progressList) { subject ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedSubjectDetails = subject },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(
                            imageVector = when (subject.subjectId) {
                                "bangla" -> Icons.Default.MenuBook
                                "english" -> Icons.Default.Translate
                                "math" -> Icons.Default.Calculate
                                "bangladesh" -> Icons.Default.Map
                                "international" -> Icons.Default.Public
                                "science" -> Icons.Default.Science
                                "ict" -> Icons.Default.Computer
                                "mental_ability" -> Icons.Default.Psychology
                                else -> Icons.Default.Stars
                            },
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isBangla) subject.titleBangla else subject.titleEnglish,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isBangla) "টপিক সম্পন্ন: ${subject.completedTopicCount}/${subject.topicCount}" else "Topics: ${subject.completedTopicCount}/${subject.topicCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = subject.progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = SaffronOrange,
                            trackColor = SaffronOrange.copy(alpha = 0.15f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${(subject.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = SaffronOrange,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectDetailSheet(subject: SubjectProgress, viewModel: BCSViewModel, onDismiss: () -> Unit) {
    val isBangla by viewModel.isBangla.collectAsState()
    var updatedSubject by remember { mutableStateOf(subject) }

    // Mock Topics List for subcategories
    val topics = when (subject.subjectId) {
        "bangla" -> listOf("চর্যাপদ ও মধ্যযুগ", "আধুনিক যুগ ও কবি পরিচিতি", "ব্যাকরণ: শব্দ ও বাক্য", "সন্ধি ও সমাস")
        "english" -> listOf("Shakespeare & Elizabethan Era", "Parts of Speech & Nouns", "Subject-Verb Agreement", "Idioms & Synonym Phrases")
        "math" -> listOf("শতকরা ও লাভ-ক্ষতি", "অনুপাত ও সমানুপাত", "বীজগণিতীয় মান নির্ণয়", "লগারিদম ও ধারা")
        else -> listOf("গুরুত্বপূর্ণ অধ্যায় ১", "গুরুত্বপূর্ণ অধ্যায় ২", "পরীক্ষায় বারবার আসা প্রশ্নসমূহ", "রিভিশন মডিউল")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = if (isBangla) subject.titleBangla else subject.titleEnglish,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = EmeraldGreen
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Subject stats card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBangla) "অগ্রগতি বিশ্লেষণ" else "Progress Tracking",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (isBangla) "টপিক সমাপ্তি হার" else "Completion Rate")
                    Text("${(updatedSubject.progress * 100).toInt()}%", fontWeight = FontWeight.Bold, color = SaffronOrange)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = updatedSubject.progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape),
                    color = SaffronOrange,
                    trackColor = SaffronOrange.copy(alpha = 0.2f)
                )
            }
        }

        // Action Quick Links
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    viewModel.selectSubjectForMcq(subject.subjectId)
                    viewModel.setScreen("mcq")
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isBangla) "এমসিকিউ অনুশীলন" else "Practice MCQ", fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = {
                    viewModel.setScreen("notes")
                },
                border = BorderStroke(1.dp, EmeraldGreen),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isBangla) "নোট তৈরি করুন" else "Create Notes", fontSize = 12.sp, color = EmeraldGreen)
            }
        }

        // Topics Checklist Title
        Text(
            text = if (isBangla) "📚 অধ্যায় ও টপিক তালিকা" else "📚 Chapter & Topic List",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        // Topic List Cards
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            topics.forEachIndexed { index, topic ->
                var isChecked by remember { mutableStateOf(index < updatedSubject.completedTopicCount) }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isChecked = !isChecked
                                val newCompCount = if (isChecked) {
                                    (updatedSubject.completedTopicCount + 1).coerceAtMost(updatedSubject.topicCount)
                                } else {
                                    (updatedSubject.completedTopicCount - 1).coerceAtLeast(0)
                                }
                                val newProgress = newCompCount.toFloat() / updatedSubject.topicCount.toFloat()
                                updatedSubject = updatedSubject.copy(
                                    completedTopicCount = newCompCount,
                                    progress = newProgress
                                )
                                viewModel.updateSubjectProgress(updatedSubject)
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                isChecked = checked
                                val newCompCount = if (checked) {
                                    (updatedSubject.completedTopicCount + 1).coerceAtMost(updatedSubject.topicCount)
                                } else {
                                    (updatedSubject.completedTopicCount - 1).coerceAtLeast(0)
                                }
                                val newProgress = newCompCount.toFloat() / updatedSubject.topicCount.toFloat()
                                updatedSubject = updatedSubject.copy(
                                    completedTopicCount = newCompCount,
                                    progress = newProgress
                                )
                                viewModel.updateSubjectProgress(updatedSubject)
                            },
                            colors = CheckboxDefaults.colors(checkedColor = EmeraldGreen)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = topic,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isBangla) "বিগত বিসিএস প্রিলিতে এখান থেকে প্রশ্ন এসেছে" else "Frequently asked in previous BCS Prelims",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlannerTab(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val tasksList by viewModel.tasks.collectAsState()

    var taskTitle by remember { mutableStateOf("") }
    var taskCategory by remember { mutableStateOf("Daily") }
    var taskDueDate by remember { mutableStateOf("আজ") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Add Task Form card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBangla) "➕ নতুন গোল / লক্ষ্য যোগ করুন" else "➕ Add New Goal / Task",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text(if (isBangla) "গোলের শিরোনাম (যেমন: চর্যাপদ পাঠ)" else "Task Goal Title") },
                    modifier = Modifier.fillMaxWidth().testTag("add_task_title"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Period Choice
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categories = listOf("Daily", "Weekly", "Monthly", "Yearly")
                    categories.forEach { cat ->
                        FilterChip(
                            selected = taskCategory == cat,
                            onClick = { taskCategory = cat },
                            label = { Text(cat) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = taskDueDate,
                    onValueChange = { taskDueDate = it },
                    label = { Text(if (isBangla) "সময়সীমা (যেমন: আজ, ৩ জুলাই)" else "Deadline (e.g. Today, July 3)") },
                    modifier = Modifier.fillMaxWidth().testTag("add_task_due"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (taskTitle.isNotBlank()) {
                            viewModel.addTask(taskTitle, taskCategory, taskDueDate)
                            taskTitle = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("add_task_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                ) {
                    Text(if (isBangla) "গোলটি যুক্ত করুন" else "Add Task")
                }
            }
        }

        // Active Goals List
        Text(
            text = if (isBangla) "📋 আপনার সক্রিয় গোলসমূহ" else "📋 Your Active Goals",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        if (tasksList.isEmpty()) {
            Text(
                text = if (isBangla) "বর্তমানে কোনো গোল তৈরি করা নেই।" else "No goals created yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tasksList.forEach { task ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Checkbox(
                                    checked = task.isCompleted,
                                    onCheckedChange = { viewModel.toggleTaskCompletion(task) },
                                    colors = CheckboxDefaults.colors(checkedColor = EmeraldGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (task.isCompleted) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onBackground
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text(
                                            text = "Category: ${task.category}",
                                            fontSize = 11.sp,
                                            color = EmeraldGreen
                                        )
                                        Text(
                                            text = "Due: ${task.dueDate}",
                                            fontSize = 11.sp,
                                            color = SaffronOrange
                                        )
                                    }
                                }
                            }

                            IconButton(onClick = { viewModel.deleteTask(task.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotebookTab(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val notesList by viewModel.notes.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var selectedSubjectId by remember { mutableStateOf("bangla") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Create Note Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBangla) "📝 দ্রুত নোট তৈরি করুন" else "📝 Quick Note taking",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = noteTitle,
                    onValueChange = { noteTitle = it },
                    label = { Text(if (isBangla) "নোটের শিরোনাম" else "Note Title") },
                    modifier = Modifier.fillMaxWidth().testTag("add_note_title"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = noteContent,
                    onValueChange = { noteContent = it },
                    label = { Text(if (isBangla) "মূল তথ্য ও নোটসমূহ (Markdown সমর্থিত)" else "Note Body (Markdown support)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("add_note_body"),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subject Selectors
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val subjects = listOf("bangla" to "বাংলা", "english" to "English", "math" to "গণিত")
                    subjects.forEach { (id, label) ->
                        FilterChip(
                            selected = selectedSubjectId == id,
                            onClick = { selectedSubjectId = id },
                            label = { Text(label) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (noteTitle.isNotBlank() && noteContent.isNotBlank()) {
                            viewModel.addNote(noteTitle, noteContent, selectedSubjectId)
                            noteTitle = ""
                            noteContent = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("add_note_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                ) {
                    Text(if (isBangla) "নোট সংরক্ষণ করুন" else "Save Note")
                }
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (isBangla) "নোট খুঁজুন..." else "Search notes...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        // Notes List Title
        Text(
            text = if (isBangla) "Saved নোটসমূহ" else "Saved Study Notes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        val filteredNotes = if (searchQuery.isBlank()) {
            notesList
        } else {
            notesList.filter { it.title.contains(searchQuery, true) || it.content.contains(searchQuery, true) }
        }

        if (filteredNotes.isEmpty()) {
            Text(
                text = if (isBangla) "কোনো নোট পাওয়া যায়নি।" else "No study notes saved.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                filteredNotes.forEach { note ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = note.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { viewModel.deleteNote(note.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.7f))
                                }
                            }
                            Text(
                                text = "Subject: ${note.subjectId.uppercase()}",
                                fontSize = 11.sp,
                                color = SaffronOrange,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = note.content,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResourcesTab(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = if (isBangla) "📂 বিনামূল্যে বিসিএস অধ্যয়ন সামগ্রী" else "📂 Free BCS Study Materials",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = EmeraldGreen
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isBangla) "ডাউনলোড ও রিভিশনের জন্য সাজানো পিডিএফ ও গাইড সমূহ" else "Downloadable booklets, guidelines, and circular references",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(viewModel.resourceList) { resource ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (resource.type) {
                            "PDF" -> Icons.Default.PictureAsPdf
                            "Video" -> Icons.Default.PlayCircle
                            "Syllabus" -> Icons.Default.Assignment
                            "Circular" -> Icons.Default.Feed
                            else -> Icons.Default.Book
                        },
                        contentDescription = null,
                        tint = SaffronOrange,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBangla) resource.titleBangla else resource.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isBangla) resource.descriptionBangla else resource.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { /* Simulation link */ },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}


// 7. Practice Hub Module (Contains: MCQ Practice, Mock Test, Leaderboard tabs)
@Composable
fun PracticeHub(viewModel: BCSViewModel, defaultTab: Int) {
    val isBangla by viewModel.isBangla.collectAsState()
    var selectedSubTab by remember { mutableStateOf(defaultTab) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = EmeraldGreen,
            contentColor = Color.White
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = { Text(if (isBangla) "এমসিকিউ কুইজ" else "MCQ Quiz", fontSize = 12.sp) }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text(if (isBangla) "মক টেস্ট" else "Mock Test", fontSize = 12.sp) }
            )
            Tab(
                selected = selectedSubTab == 2,
                onClick = { selectedSubTab = 2 },
                text = { Text(if (isBangla) "লিডারবোর্ড" else "Leaderboard", fontSize = 12.sp) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedSubTab) {
                0 -> McqPracticeTab(viewModel)
                1 -> MockTestTab(viewModel)
                2 -> LeaderboardTab(viewModel)
            }
        }
    }
}

@Composable
fun McqPracticeTab(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val selectedSubject by viewModel.selectedSubjectForMcq.collectAsState()
    val mcqQuestionsList by viewModel.mcqs.collectAsState()
    val currentIndex by viewModel.currentMcqIndex.collectAsState()
    val selectedOption by viewModel.selectedOptionIndex.collectAsState()
    val isAnswered by viewModel.isMcqAnswered.collectAsState()
    val statsCorrect by viewModel.mcqStatsCorrect.collectAsState()
    val statsTotal by viewModel.mcqStatsTotal.collectAsState()

    // Filter questions based on selected subject filter
    val filteredMcqs = if (selectedSubject == "all" || selectedSubject == null) {
        mcqQuestionsList
    } else {
        mcqQuestionsList.filter { it.subjectId == selectedSubject }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Horizontal Subject Filter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = listOf("all" to "সব", "bangla" to "বাংলা", "english" to "English", "math" to "গণিত", "bangladesh" to "বাংলাদেশ")
            filters.forEach { (id, label) ->
                FilterChip(
                    selected = selectedSubject == id,
                    onClick = { viewModel.selectSubjectForMcq(id) },
                    label = { Text(label) }
                )
            }
        }

        if (filteredMcqs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isBangla) "দুঃখিত, এই ক্যাটাগরিতে কোনো এমসিকিউ পাওয়া যায়নি।" else "No questions in this filter.",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            val activeQuestion = filteredMcqs[currentIndex.coerceAtMost(filteredMcqs.size - 1)]

            // Question Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Question ${currentIndex + 1}/${filteredMcqs.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SaffronOrange,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { viewModel.bookmarkMcqToggle(activeQuestion) }) {
                            Icon(
                                imageVector = if (activeQuestion.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (activeQuestion.isBookmarked) SaffronOrange else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isBangla) activeQuestion.questionBangla else activeQuestion.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Options List
            val options = if (isBangla) activeQuestion.getOptionsBangla() else activeQuestion.getOptions()
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                options.forEachIndexed { optIndex, optionText ->
                    val isCorrect = optIndex == activeQuestion.correctOptionIndex
                    val isSelected = optIndex == selectedOption

                    val cardColor = when {
                        isAnswered && isCorrect -> Color(0xFFC8E6C9) // Green for correct answer
                        isAnswered && isSelected && !isCorrect -> Color(0xFFFFCDD2) // Red for selected wrong answer
                        isSelected -> EmeraldGreen.copy(alpha = 0.15f)
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val borderColor = when {
                        isAnswered && isCorrect -> Color(0xFF4CAF50)
                        isAnswered && isSelected && !isCorrect -> Color(0xFFF44336)
                        isSelected -> EmeraldGreen
                        else -> Color.Transparent
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.chooseMcqOption(optIndex) }
                            .testTag("mcq_option_$optIndex"),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        border = BorderStroke(1.5.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { viewModel.chooseMcqOption(optIndex) },
                                colors = RadioButtonDefaults.colors(selectedColor = EmeraldGreen)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = optionText, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Submit / Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Score text
                Text(
                    text = if (isBangla) "স্কোর: $statsCorrect/$statsTotal" else "Score: $statsCorrect/$statsTotal",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen
                )

                if (!isAnswered) {
                    Button(
                        onClick = { viewModel.submitMcqAnswer(activeQuestion.correctOptionIndex) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        enabled = selectedOption != null,
                        modifier = Modifier.testTag("mcq_submit_btn")
                    ) {
                        Text(if (isBangla) "উত্তর জমা দিন" else "Check Answer")
                    }
                } else {
                    Button(
                        onClick = { viewModel.nextMcq(filteredMcqs.size) },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronOrange),
                        modifier = Modifier.testTag("mcq_next_btn")
                    ) {
                        Text(if (isBangla) "পরবর্তী প্রশ্ন" else "Next Question")
                    }
                }
            }

            // Explanation Section if Answered
            if (isAnswered) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = EmeraldGreen.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isBangla) "💡 ব্যাখ্যা ও বিশ্লেষণ" else "💡 Detailed Explanation",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = activeQuestion.explanation, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun MockTestTab(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val isActive by viewModel.isMockTestActive.collectAsState()
    val timeLeft by viewModel.mockTestTimeLeft.collectAsState()
    val mockQuestions by viewModel.mcqs.collectAsState()
    val activeIndex by viewModel.activeMockIndex.collectAsState()
    val chosenAnswers by viewModel.mockAnswers.collectAsState()
    val mockResults by viewModel.mockTestResults.collectAsState()

    val testQuestions = mockQuestions.take(5) // 5 questions for simple prototyping test

    if (isActive) {
        // Active Mock Exam View
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header timer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBangla) "বিসিএস প্রিলি কাস্টম মক" else "BCS Prelims Custom Mock",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Timer card
                val mins = timeLeft / 60
                val secs = timeLeft % 60
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, Color.Red)
                ) {
                    Text(
                        text = String.format("%02d:%02d", mins, secs),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
            }

            // Question numbers grid
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(testQuestions.size) { i ->
                    val isVisited = chosenAnswers.containsKey(i)
                    val color = if (i == activeIndex) EmeraldGreen else (if (isVisited) SaffronGold else MaterialTheme.colorScheme.surface)
                    Button(
                        onClick = { viewModel.setMockIndex(i) },
                        colors = ButtonDefaults.buttonColors(containerColor = color),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text((i + 1).toString(), color = Color.White)
                    }
                }
            }

            // Question card
            val currentQ = testQuestions[activeIndex]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Question ${activeIndex + 1} of ${testQuestions.size}",
                        fontSize = 12.sp,
                        color = SaffronOrange,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isBangla) currentQ.questionBangla else currentQ.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Options selection list
            val options = if (isBangla) currentQ.getOptionsBangla() else currentQ.getOptions()
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                options.forEachIndexed { optIndex, text ->
                    val isSelected = chosenAnswers[activeIndex] == optIndex
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.chooseMockAnswer(activeIndex, optIndex) },
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) EmeraldGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.5.dp, if (isSelected) EmeraldGreen else Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { viewModel.chooseMockAnswer(activeIndex, optIndex) },
                                colors = RadioButtonDefaults.colors(selectedColor = EmeraldGreen)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = text, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            // Submit Exam
            Button(
                onClick = { viewModel.submitMockTest() },
                modifier = Modifier.fillMaxWidth().testTag("mock_submit_exam"),
                colors = ButtonDefaults.buttonColors(containerColor = SaffronOrange)
            ) {
                Text(if (isBangla) "পরীক্ষা সম্পন্ন ও সাবমিট করুন" else "Complete & Submit Exam", fontWeight = FontWeight.Bold)
            }
        }
    } else {
        // Welcome and previous mock results view
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Promo Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = EmeraldGreen)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isBangla) "🏁 বিসিএস মডেল মক টেস্ট" else "🏁 BCS Model Mock Test",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isBangla) "আসল পরীক্ষার মতো ঘড়ি ধরে মক টেস্ট দিন। ভুল উত্তরের জন্য ০.২৫ নেগেটিভ মার্কিং হিসাব করা হবে।" else "Simulated live exam parameters, automatic 0.25 negative marks calculation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.startMockTest() },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronGold),
                        modifier = Modifier.testTag("start_mock_btn")
                    ) {
                        Text(if (isBangla) "পরীক্ষা শুরু করুন" else "Start Mock Exam", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Test Results Title
            Text(
                text = if (isBangla) "📊 বিগত পরীক্ষার ফলাফল বিশ্লেষণ" else "📊 Previous Mock Results Analysis",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (mockResults.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (isBangla) "এখনো কোনো মক টেস্ট দেওয়া হয়নি।" else "No exam history found.")
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    mockResults.forEach { result ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = result.testTitle, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = if (isBangla) "প্রাপ্ত নম্বর: ${result.score}" else "Score: ${result.score}",
                                        color = EmeraldGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Text(
                                        text = if (isBangla) "সঠিক: ${result.correctAnswers}" else "Correct: ${result.correctAnswers}",
                                        fontSize = 12.sp,
                                        color = Color.Green
                                    )
                                    Text(
                                        text = if (isBangla) "ভুল: ${result.wrongAnswers}" else "Wrong: ${result.wrongAnswers}",
                                        fontSize = 12.sp,
                                        color = Color.Red
                                    )
                                }
                                if (result.weakSubject != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isBangla) "⚠️ দুর্বল স্থান: ${result.weakSubject}। অধ্যায়ে জোর দিন।" else "⚠️ Weakness: ${result.weakSubject}. Focus on this subject.",
                                        fontSize = 12.sp,
                                        color = SaffronOrange,
                                        fontWeight = FontWeight.Bold
                                    )
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
fun LeaderboardTab(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = if (isBangla) "🏆 সর্বমোট লিডারবোর্ড" else "🏆 Global Leaderboard",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = EmeraldGreen
            )
            Text(
                text = if (isBangla) "সারা দেশের চাকরিপ্রার্থীদের মাঝে আপনার অবস্থান" else "Compete live with BCS candidates nationwide",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(viewModel.leaderboard) { entry ->
            val color = if (entry.isCurrentUser) SaffronGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
            val border = if (entry.isCurrentUser) BorderStroke(1.5.dp, SaffronGold) else null

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = color),
                border = border
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "#${entry.rank}",
                            fontWeight = FontWeight.Bold,
                            color = SaffronOrange,
                            modifier = Modifier.width(36.dp)
                        )
                        Text(
                            text = entry.name,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Text(
                        text = if (isBangla) "${entry.score} পয়েন্ট" else "${entry.score} pts",
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


// 8. AI Mentor & Current Affairs Module (Contains tabs)
@Composable
fun MentorHub(viewModel: BCSViewModel, defaultTab: Int) {
    val isBangla by viewModel.isBangla.collectAsState()
    var selectedSubTab by remember { mutableStateOf(defaultTab) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = EmeraldGreen,
            contentColor = Color.White
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = { Text(if (isBangla) "এআই মেন্টর" else "AI Mentor", fontSize = 12.sp) }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text(if (isBangla) "সাম্প্রতিক তথ্য" else "Current Affairs", fontSize = 12.sp) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedSubTab) {
                0 -> AiMentorTab(viewModel)
                1 -> CurrentAffairsTab(viewModel)
            }
        }
    }
}

@Composable
fun AiMentorTab(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val messages by viewModel.aiMessages.collectAsState()
    val isLoading by viewModel.aiMentorLoading.collectAsState()

    var userMessageText by remember { mutableStateOf("") }
    val listState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Chat History Scrollable Area
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(listState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            messages.forEach { msg ->
                val isUser = msg.sender == "user"
                val alignment = if (isUser) Alignment.End else Alignment.Start
                val bubbleColor = if (isUser) SaffronOrange.copy(alpha = 0.15f) else EmeraldGreen.copy(alpha = 0.1f)
                val border = BorderStroke(1.dp, if (isUser) SaffronOrange else EmeraldGreen)

                Column(modifier = Modifier.align(alignment)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = bubbleColor),
                        border = border,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 0.dp,
                            bottomEnd = if (isUser) 0.dp else 16.dp
                        )
                    ) {
                        Text(
                            text = msg.text,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = if (isUser) (if (isBangla) "আপনি" else "You") else (if (isBangla) "বিসিএস মেন্টর" else "BCS Mentor"),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = EmeraldGreen)
                }
            }
        }

        // Suggestions Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = if (isBangla) "💡 মেন্টরকে জিজ্ঞাসা করতে পারেন:" else "💡 Suggested prompts:",
                    fontSize = 11.sp,
                    color = SaffronOrange,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val sug1 = if (isBangla) "৪৬তম বিসিএস পরীক্ষার কৌশল দিন" else "46th BCS Preparation Strategy"
                    val sug2 = if (isBangla) "চর্যাপদ কেন এত গুরুত্বপূর্ণ?" else "Explain Charyapada"
                    val sug3 = if (isBangla) "ইংরেজি সাহিত্যের পড়ার রুটিন" else "English literature tips"

                    Button(
                        onClick = { viewModel.sendMessageToAI(sug1) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(sug1, fontSize = 11.sp, color = EmeraldGreen)
                    }
                    Button(
                        onClick = { viewModel.sendMessageToAI(sug2) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(sug2, fontSize = 11.sp, color = EmeraldGreen)
                    }
                    Button(
                        onClick = { viewModel.sendMessageToAI(sug3) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(sug3, fontSize = 11.sp, color = EmeraldGreen)
                    }
                }
            }
        }

        // Send message input row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.clearChat() }) {
                Icon(Icons.Default.DeleteSweep, contentDescription = "Clear Chat", tint = Color.Red.copy(alpha = 0.7f))
            }

            OutlinedTextField(
                value = userMessageText,
                onValueChange = { userMessageText = it },
                placeholder = { Text(if (isBangla) "মেন্টরকে যেকোনো প্রশ্ন করুন..." else "Ask BCS Mentor...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("mentor_chat_input"),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (userMessageText.isNotBlank()) {
                        viewModel.sendMessageToAI(userMessageText)
                        userMessageText = ""
                    }
                },
                modifier = Modifier.testTag("mentor_send_btn")
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = EmeraldGreen)
            }
        }
    }
}

@Composable
fun CurrentAffairsTab(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val affairs by viewModel.currentAffairs.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = if (isBangla) "📰 সাম্প্রতিক দেশীয় ও আন্তর্জাতিক তথ্য" else "📰 Daily & Monthly Current Affairs",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = EmeraldGreen
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(affairs) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (item.category == "Bangladesh") Icons.Default.Map else Icons.Default.Public,
                                contentDescription = null,
                                tint = SaffronOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.category.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaffronOrange
                            )
                        }

                        IconButton(onClick = { viewModel.toggleCurrentAffairBookmark(item) }) {
                            Icon(
                                imageVector = if (item.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (item.isBookmarked) SaffronOrange else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isBangla) item.titleBangla else item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isBangla) item.contentBangla else item.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Published: ${item.date}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}


// 9. Profile & Settings Hub
@Composable
fun ProfileHub(viewModel: BCSViewModel, defaultTab: Int) {
    val isBangla by viewModel.isBangla.collectAsState()
    var selectedSubTab by remember { mutableStateOf(defaultTab) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = EmeraldGreen,
            contentColor = Color.White
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = { Text(if (isBangla) "আমার প্রোফাইল" else "My Profile", fontSize = 12.sp) }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text(if (isBangla) "সেটিংস" else "Settings", fontSize = 12.sp) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedSubTab) {
                0 -> ProfileTab(viewModel)
                1 -> SettingsTab(viewModel)
            }
        }
    }
}

@Composable
fun ProfileTab(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Avatar circle
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(EmeraldGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(54.dp))
        }

        Text(
            text = currentUser ?: "BCS Candidate",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(SaffronGold.copy(alpha = 0.15f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Stars, contentDescription = null, tint = SaffronOrange, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isBangla) "টার্গেট ক্যাডার: বিসিএস প্রসাশন (BCS Admin)" else "Target Choice: BCS Admin",
                style = MaterialTheme.typography.bodySmall,
                color = SaffronOrange,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Unlockable Achievements Badges
        Text(
            text = if (isBangla) "🎖️ অর্জন ও মেডেল সমূহ" else "🎖️ Unlocked Badges",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BadgeItem(label = if (isBangla) "ভোর পাখি" else "Early Bird", desc = "Studied at 5 AM", icon = "☀️", modifier = Modifier.weight(1f))
            BadgeItem(label = if (isBangla) "ডেইলি লাভার" else "Goal Winner", desc = "Completed 5 goals", icon = "🏆", modifier = Modifier.weight(1f))
            BadgeItem(label = if (isBangla) "এআই জাদুকর" else "AI Chat Fan", desc = "Asked AI 10 times", icon = "🧠", modifier = Modifier.weight(1f))
        }

        // Simple Stat logs card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBangla) "📈 সামগ্রিক বিসিএস প্রস্তুতি সামারি" else "📈 Overall Preparatory Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (isBangla) "মোট পড়াশোনা ঘণ্টা" else "Total study hours")
                    Text("৪৫.৫ ঘণ্টা", fontWeight = FontWeight.Bold, color = EmeraldGreen)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (isBangla) "সঠিক এমসিকিউ হার" else "MCQ Accuracy")
                    Text("৭৫%", fontWeight = FontWeight.Bold, color = SaffronOrange)
                }
            }
        }
    }
}

@Composable
fun BadgeItem(label: String, desc: String, icon: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, SaffronGold)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
            Text(text = desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun SettingsTab(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (isBangla) "⚙️ অ্যাপ্লিকেশন কনফিগারেশন" else "⚙️ App Preferences",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = EmeraldGreen
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Language Choice Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(if (isBangla) "ভাষা পরিবর্তন (Language)" else "App Language", fontWeight = FontWeight.Bold)
                        Text(if (isBangla) "বাংলা / English পরিবর্তন করুন" else "Toggle Bangla / English", fontSize = 11.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                    }
                    Switch(
                        checked = !isBangla,
                        onCheckedChange = { viewModel.toggleLanguage() },
                        colors = SwitchDefaults.colors(checkedThumbColor = EmeraldGreen)
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                // Theme choice Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(if (isBangla) "ডার্ক মোড (Dark Mode)" else "Dark Mode Theme", fontWeight = FontWeight.Bold)
                        Text(if (isBangla) "স্ক্রিনের চোখের সুরক্ষায় ডার্ক মোড অন করুন" else "Toggle eye-safe night mode theme", fontSize = 11.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode() },
                        colors = SwitchDefaults.colors(checkedThumbColor = EmeraldGreen)
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                // Pomodoro configuration Choice
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(if (isBangla) "পোমোডোরো সেশন দৈর্ঘ্য" else "Pomodoro Session length", fontWeight = FontWeight.Bold)
                        Text(if (isBangla) "২৫ মিনিট নির্ধারণ করা আছে" else "Default study length is 25 minutes", fontSize = 11.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                    }
                    Text("25M", fontWeight = FontWeight.Bold, color = SaffronOrange)
                }
            }
        }

        // Safety warning as requested by secret-management guidelines for APK builds
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SaffronOrange.copy(alpha = 0.08f)),
            border = BorderStroke(1.dp, SaffronOrange.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = "Security", tint = SaffronOrange)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isBangla) "নিরাপত্তা সতর্কতা" else "Security Warning", fontWeight = FontWeight.Bold, color = SaffronOrange)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "I have included your API keys in the generated APK file for this prototype. Please be aware that Android APKs can be easily decompiled, and these keys can be extracted by anyone who has access to the file. Do not share this APK file publicly or with unauthorized individuals to prevent potential misuse.",
                    fontSize = 11.sp
                )
            }
        }
    }
}


// 10. Admin Panel Screen
@Composable
fun AdminPanelScreen(viewModel: BCSViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    var announcementInput by remember { mutableStateOf("") }
    var mockCreatedMsg by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.setScreen("dashboard") }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = if (isBangla) "🛠️ অ্যাডমিন কন্ট্রোল প্যানেল" else "🛠️ Admin Moderator Control",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = EmeraldGreen
            )
        }

        // Admin Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AdminStatCard(title = if (isBangla) "মোট সদস্য" else "Total Users", value = "1,452", modifier = Modifier.weight(1f))
            AdminStatCard(title = if (isBangla) "মোট কুইজ" else "MCQ Count", value = "9", modifier = Modifier.weight(1f))
            AdminStatCard(title = if (isBangla) "মক টেস্ট" else "Mocks Held", value = "5", modifier = Modifier.weight(1f))
        }

        // Publisher section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBangla) "📢 নতুন ড্যাশবোর্ড নোটিশ প্রকাশ করুন" else "📢 Publish New Dashboard Announcement",
                    fontWeight = FontWeight.Bold,
                    color = SaffronOrange
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = announcementInput,
                    onValueChange = { announcementInput = it },
                    label = { Text(if (isBangla) "ঘোষণার মূল বিষয় লিখুন..." else "Announcement headline...") },
                    modifier = Modifier.fillMaxWidth().testTag("admin_announce_input"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (announcementInput.isNotBlank()) {
                            viewModel.addAnnouncement(announcementInput)
                            announcementInput = ""
                            mockCreatedMsg = if (isBangla) "ঘোষণাটি সফলভাবে প্রকাশিত হয়েছে!" else "Notice published successfully!"
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("admin_publish_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                ) {
                    Text(if (isBangla) "ঘোষণা প্রকাশ করুন" else "Publish Notice")
                }
            }
        }

        // Simulation management card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBangla) "⚙️ মডারেশন অপশন" else "⚙️ Platform Moderator Options",
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { mockCreatedMsg = if (isBangla) "নতুন ৪৬তম বিসিএস মক টেস্ট সেট ৩ যুক্ত হয়েছে!" else "New BCS Mock test set 3 injected!" },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronOrange),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (isBangla) "+ মক টেস্ট যুক্ত করুন" else "+ Add Mock Test", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = { mockCreatedMsg = if (isBangla) "মডারেশন সিঙ্ক এবং ক্যাশ ক্লিয়ার সম্পন্ন!" else "Cache synced & cleared!" },
                        border = BorderStroke(1.dp, EmeraldGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (isBangla) "সিঙ্ক রিকোয়েস্ট" else "Cache Sync", fontSize = 11.sp, color = EmeraldGreen)
                    }
                }
            }
        }

        if (mockCreatedMsg != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Green.copy(alpha = 0.1f)),
                border = BorderStroke(1.dp, Color.Green)
            ) {
                Text(
                    text = mockCreatedMsg!!,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = EmeraldGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AdminStatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = EmeraldGreen)
        }
    }
}

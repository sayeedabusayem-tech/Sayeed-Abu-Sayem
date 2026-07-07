package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.model.Task
import com.example.data.model.SubjectProgress
import com.example.data.model.McqQuestion
import com.example.data.model.MockTestResult
import com.example.data.model.Note
import com.example.data.model.CurrentAffairItem
import com.example.data.model.ResourceItem
import com.example.data.model.LeaderboardEntry
import com.example.data.remote.GeminiClient
import com.example.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BCSViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "bcs_companion_db"
    ).fallbackToDestructiveMigration().build()

    private val repository = AppRepository(db.appDao())

    // Language & Themes
    private val _isBangla = MutableStateFlow(true)
    val isBangla: StateFlow<Boolean> = _isBangla.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Current Screen / Route
    private val _currentScreen = MutableStateFlow("landing") // landing, login, register, dashboard, study_planner, subjects, mcq, mock_test, analytics, notes, affairs, ai_mentor, resources, profile, settings, admin
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    // Authentication States
    private val _currentUser = MutableStateFlow<String?>(null) // Username if logged in
    val currentUser: StateFlow<String?> = _currentUser.asStateFlow()

    // Room DB Observables
    val tasks: StateFlow<List<Task>> = repository.allTasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val subjectProgressList: StateFlow<List<SubjectProgress>> = repository.allSubjectProgress.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val mcqs: StateFlow<List<McqQuestion>> = repository.allMcqs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val bookmarkedMcqs: StateFlow<List<McqQuestion>> = repository.bookmarkedMcqs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val mockTestResults: StateFlow<List<MockTestResult>> = repository.allMockTestResults.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val notes: StateFlow<List<Note>> = repository.allNotes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val currentAffairs: StateFlow<List<CurrentAffairItem>> = repository.allCurrentAffairs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val bookmarkedCurrentAffairs: StateFlow<List<CurrentAffairItem>> = repository.bookmarkedCurrentAffairs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Pomodoro Timer State
    private val _pomodoroSecondsLeft = MutableStateFlow(1500) // 25 mins
    val pomodoroSecondsLeft: StateFlow<Int> = _pomodoroSecondsLeft.asStateFlow()

    private val _pomodoroRunning = MutableStateFlow(false)
    val pomodoroRunning: StateFlow<Boolean> = _pomodoroRunning.asStateFlow()

    private val _pomodoroMode = MutableStateFlow("Study") // "Study", "Break"
    val pomodoroMode: StateFlow<String> = _pomodoroMode.asStateFlow()

    private val _pomodoroSessions = MutableStateFlow(3)
    val pomodoroSessions: StateFlow<Int> = _pomodoroSessions.asStateFlow()

    private var pomodoroJob: Job? = null

    // Streak and Daily study hours simulation
    val studyStreak = MutableStateFlow(12)
    val dailyHoursToday = MutableStateFlow(4.5f)

    // MCQ Practice state
    private val _selectedSubjectForMcq = MutableStateFlow<String?>("all")
    val selectedSubjectForMcq: StateFlow<String?> = _selectedSubjectForMcq.asStateFlow()

    private val _currentMcqIndex = MutableStateFlow(0)
    val currentMcqIndex: StateFlow<Int> = _currentMcqIndex.asStateFlow()

    private val _selectedOptionIndex = MutableStateFlow<Int?>(null)
    val selectedOptionIndex: StateFlow<Int?> = _selectedOptionIndex.asStateFlow()

    private val _isMcqAnswered = MutableStateFlow(false)
    val isMcqAnswered: StateFlow<Boolean> = _isMcqAnswered.asStateFlow()

    private val _mcqStatsCorrect = MutableStateFlow(0)
    val mcqStatsCorrect: StateFlow<Int> = _mcqStatsCorrect.asStateFlow()

    private val _mcqStatsTotal = MutableStateFlow(0)
    val mcqStatsTotal: StateFlow<Int> = _mcqStatsTotal.asStateFlow()

    // Mock Test State
    private val _isMockTestActive = MutableStateFlow(false)
    val isMockTestActive: StateFlow<Boolean> = _isMockTestActive.asStateFlow()

    private val _mockTestTimeLeft = MutableStateFlow(600) // 10 minutes for mock
    val mockTestTimeLeft: StateFlow<Int> = _mockTestTimeLeft.asStateFlow()

    private val _mockTestScore = MutableStateFlow(0f)
    val mockTestScore: StateFlow<Float> = _mockTestScore.asStateFlow()

    private val _mockTestCorrect = MutableStateFlow(0)
    val mockTestCorrect: StateFlow<Int> = _mockTestCorrect.asStateFlow()

    private val _mockTestWrong = MutableStateFlow(0)
    val mockTestWrong: StateFlow<Int> = _mockTestWrong.asStateFlow()

    private val _activeMockIndex = MutableStateFlow(0)
    val activeMockIndex: StateFlow<Int> = _activeMockIndex.asStateFlow()

    private val _mockAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap()) // map of question index to chosen option index
    val mockAnswers: StateFlow<Map<Int, Int>> = _mockAnswers.asStateFlow()

    private var mockTestTimerJob: Job? = null

    // AI Mentor Chat
    data class ChatMessage(val sender: String, val text: String, val timestamp: Long = System.currentTimeMillis())
    private val _aiMessages = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage("mentor", "হ্যালো! আমি আপনার বিসিএস এআই মেন্টর। বাংলাদেশ বিষয়াবলি, বাংলা সাহিত্য, বিজ্ঞান বা যেকোনো বিসিএস প্রশ্ন ও প্রস্তুতির কৌশল সম্পর্কে আমাকে জিজ্ঞাসা করুন।")
    ))
    val aiMessages: StateFlow<List<ChatMessage>> = _aiMessages.asStateFlow()

    private val _aiMentorLoading = MutableStateFlow(false)
    val aiMentorLoading: StateFlow<Boolean> = _aiMentorLoading.asStateFlow()

    // Static Resource items
    val resourceList = listOf(
        ResourceItem(
            "BCS Syllabus Overview (Preliminary & Written)",
            "বিসিএস সিলেবাস ওভারভিউ (প্রিলি ও লিখিত)",
            "Syllabus",
            "Official syllabus issued by BPSC detailing marks distribution and chapters.",
            "বাংলাদেশ সরকারি কর্ম কমিশন (BPSC) কর্তৃক প্রকাশিত পূর্ণাঙ্গ সিলেবাস ও মানবন্টন।",
            "https://bpsc.gov.bd"
        ),
        ResourceItem(
            "46th BCS Preliminary Official Circular",
            "৪৬তম বিসিএস প্রিলিমিনারি সার্কুলার",
            "Circular",
            "Full circular guidelines, deadlines, and seat distribution detail PDF.",
            "৪৬তম বিসিএস পরীক্ষার নিয়মাবলী, আবেদনের শেষ সময় ও নির্দেশনা সম্বলিত পিডিএফ।",
            "https://bpsc.gov.bd"
        ),
        ResourceItem(
            "MP3 Bangladesh Affairs Reference Guide",
            "এমপি৩ বাংলাদেশ বিষয়াবলি গাইড বুক",
            "Book",
            "Essential preparatory textbook containing historical records and stats of Bangladesh.",
            "বিসিএস ও অন্যান্য সরকারি চাকরির জন্য অত্যন্ত দরকারি তথ্যপূর্ণ রেফারেন্স বই।",
            "https://bcscompanion.com"
        ),
        ResourceItem(
            "Mathematics Shortcuts & Formulae PDF",
            "বিসিএস গণিত শর্টকাট ও সূত্র সমগ্র",
            "PDF",
            "Formula sheets and shortcut methodologies to solve mathematics quickly in prelims.",
            "পরীক্ষায় দ্রুত গণিত সমাধানের প্রয়োজনীয় সূত্র ও শর্টকাট টেকনিক সংকলন পিডিএফ।",
            "https://bcscompanion.com"
        )
    )

    // Static Leaderboard
    val leaderboard = listOf(
        LeaderboardEntry(1, "মো: রাইহান রহমান", 2450),
        LeaderboardEntry(2, "সানজিদা আক্তার লিজা", 2320),
        LeaderboardEntry(3, "আবু সায়েম শেখ", 2280),
        LeaderboardEntry(4, "বিসিএস ড্রিমার (CurrentUser)", 2120, true),
        LeaderboardEntry(5, "তাসনিম জাহান লুবনা", 1980),
        LeaderboardEntry(6, "ফারহান আহমেদ স্বাধীন", 1850)
    )

    // Admin Panel Announcements
    private val _announcements = MutableStateFlow<List<String>>(listOf(
        "৪৬তম বিসিএস প্রিলিমিনারি পরীক্ষা আগামী মাসে অনুষ্ঠিত হবে। জোর প্রস্তুতি বজায় রাখুন!",
        "আজকের সাম্প্রতিক তথ্য মডিউল আপডেট করা হয়েছে। বিশ্ব জলবায়ু সম্মেলন সম্পর্কিত গুরুত্বপূর্ণ প্রশ্নসমূহ দেখে নিন।"
    ))
    val announcements: StateFlow<List<String>> = _announcements.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.populateInitialDataIfEmpty()
        }
    }

    // Navigation and Language UI events
    fun setScreen(screen: String) {
        _currentScreen.value = screen
    }

    fun toggleLanguage() {
        _isBangla.value = !_isBangla.value
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    // Simulated Authentication
    fun login(username: String, pass: String): Boolean {
        if (username.isNotBlank()) {
            _currentUser.value = username
            _currentScreen.value = "dashboard"
            return true
        }
        return false
    }

    fun register(username: String, email: String, pass: String): Boolean {
        if (username.isNotBlank() && email.contains("@")) {
            _currentUser.value = username
            _currentScreen.value = "dashboard"
            return true
        }
        return false
    }

    fun logout() {
        _currentUser.value = null
        _currentScreen.value = "landing"
    }

    // Tasks/Planner Events
    fun addTask(title: String, category: String, dueDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = Task(title = title, category = category, dueDate = dueDate)
            repository.insertTask(task)
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = task.copy(
                isCompleted = !task.isCompleted,
                completionDate = if (!task.isCompleted) SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) else null
            )
            repository.updateTask(updated)

            // Dynamic progress updating when a task for a subject or general is checked
            if (updated.isCompleted) {
                // Mock simple achievement
                studyStreak.value += 1
                dailyHoursToday.value += 0.5f
            } else {
                if (studyStreak.value > 0) studyStreak.value -= 1
                if (dailyHoursToday.value > 0.5f) dailyHoursToday.value -= 0.5f
            }
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTaskById(id)
        }
    }

    // Pomodoro Timer Events
    fun togglePomodoro() {
        if (_pomodoroRunning.value) {
            pomodoroJob?.cancel()
            _pomodoroRunning.value = false
        } else {
            _pomodoroRunning.value = true
            pomodoroJob = viewModelScope.launch(Dispatchers.Default) {
                while (_pomodoroSecondsLeft.value > 0) {
                    delay(1000)
                    _pomodoroSecondsLeft.value -= 1
                }
                // Completed session
                _pomodoroRunning.value = false
                if (_pomodoroMode.value == "Study") {
                    _pomodoroMode.value = "Break"
                    _pomodoroSecondsLeft.value = 300 // 5 min break
                    _pomodoroSessions.value += 1
                    dailyHoursToday.value += 0.4f
                } else {
                    _pomodoroMode.value = "Study"
                    _pomodoroSecondsLeft.value = 1500 // 25 min study
                }
            }
        }
    }

    fun resetPomodoro() {
        pomodoroJob?.cancel()
        _pomodoroRunning.value = false
        _pomodoroMode.value = "Study"
        _pomodoroSecondsLeft.value = 1500
    }

    // MCQ Practice Events
    fun selectSubjectForMcq(subjectId: String?) {
        _selectedSubjectForMcq.value = subjectId
        _currentMcqIndex.value = 0
        _selectedOptionIndex.value = null
        _isMcqAnswered.value = false
    }

    fun chooseMcqOption(optionIndex: Int) {
        if (_isMcqAnswered.value) return
        _selectedOptionIndex.value = optionIndex
    }

    fun submitMcqAnswer(correctIndex: Int) {
        if (_selectedOptionIndex.value == null || _isMcqAnswered.value) return
        _isMcqAnswered.value = true
        _mcqStatsTotal.value += 1
        if (_selectedOptionIndex.value == correctIndex) {
            _mcqStatsCorrect.value += 1
            // Random progress increase for dynamic feeling
            viewModelScope.launch(Dispatchers.IO) {
                val current = repository.allSubjectProgress.first()
                val selectedSubId = _selectedSubjectForMcq.value ?: "bangla"
                val match = current.find { it.subjectId == selectedSubId }
                if (match != null) {
                    val comp = (match.completedTopicCount + 1).coerceAtMost(match.topicCount)
                    repository.updateSubjectProgress(match.copy(
                        completedTopicCount = comp,
                        progress = comp.toFloat() / match.topicCount.toFloat()
                    ))
                }
            }
        }
    }

    fun nextMcq(totalQuestions: Int) {
        _currentMcqIndex.value = (_currentMcqIndex.value + 1) % totalQuestions
        _selectedOptionIndex.value = null
        _isMcqAnswered.value = false
    }

    fun bookmarkMcqToggle(mcq: McqQuestion) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMcq(mcq.copy(isBookmarked = !mcq.isBookmarked))
        }
    }

    // Mock Test Events
    fun startMockTest() {
        _isMockTestActive.value = true
        _mockTestTimeLeft.value = 600 // 10 minutes (600 seconds)
        _mockAnswers.value = emptyMap()
        _activeMockIndex.value = 0
        _mockTestScore.value = 0f
        _mockTestCorrect.value = 0
        _mockTestWrong.value = 0

        mockTestTimerJob = viewModelScope.launch(Dispatchers.Default) {
            while (_mockTestTimeLeft.value > 0 && _isMockTestActive.value) {
                delay(1000)
                _mockTestTimeLeft.value -= 1
            }
            if (_isMockTestActive.value) {
                submitMockTest()
            }
        }
    }

    fun chooseMockAnswer(questionIndex: Int, optionIndex: Int) {
        val updated = _mockAnswers.value.toMutableMap()
        updated[questionIndex] = optionIndex
        _mockAnswers.value = updated
    }

    fun setMockIndex(index: Int) {
        _activeMockIndex.value = index
    }

    fun submitMockTest() {
        mockTestTimerJob?.cancel()
        _isMockTestActive.value = false

        // Compute results
        val answers = _mockAnswers.value
        var correct = 0
        var wrong = 0
        var score = 0f

        viewModelScope.launch(Dispatchers.IO) {
            val allQuestions = repository.allMcqs.first()
            val activeMockQuestions = allQuestions.take(5) // Simple mock test with 5 questions

            activeMockQuestions.forEachIndexed { idx, q ->
                val chosen = answers[idx]
                if (chosen != null) {
                    if (chosen == q.correctOptionIndex) {
                        correct += 1
                        score += 1.0f
                    } else {
                        wrong += 1
                        score -= 0.25f // Negative marking
                    }
                }
            }

            _mockTestCorrect.value = correct
            _mockTestWrong.value = wrong
            _mockTestScore.value = score.coerceAtLeast(0f)

            // Save results to DB
            repository.insertMockTestResult(
                MockTestResult(
                    testTitle = "BCS Prelims Custom Mock Test",
                    totalQuestions = activeMockQuestions.size,
                    score = score.coerceAtLeast(0f),
                    correctAnswers = correct,
                    wrongAnswers = wrong,
                    durationSeconds = 600 - _mockTestTimeLeft.value,
                    weakSubject = if (wrong > correct) "English Literature" else "Mathematics"
                )
            )

            // Award achievements
            _currentScreen.value = "mock_test" // Show success state in Mock Test page
        }
    }

    // Notes Events
    fun addNote(title: String, content: String, subjectId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNote(Note(title = title, content = content, subjectId = subjectId))
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNoteById(id)
        }
    }

    // Current Affairs bookmarking
    fun toggleCurrentAffairBookmark(item: CurrentAffairItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCurrentAffair(item.copy(isBookmarked = !item.isBookmarked))
        }
    }

    // AI Chat events
    fun sendMessageToAI(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage("user", text)
        val updated = _aiMessages.value.toMutableList()
        updated.add(userMsg)
        _aiMessages.value = updated

        _aiMentorLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val systemInstructions = "You are BCS Companion AI Mentor. Assist user on BCS prelims syllabus, study planning, explaining Bangladesh affairs, Bangla literature, math tricks, English grammar, and motivation. Answer in Bangla or English, based on the question."
            val reply = GeminiClient.askMentor(text, systemInstructions)
            
            _aiMessages.value = _aiMessages.value.toMutableList().apply {
                add(ChatMessage("mentor", reply))
            }
            _aiMentorLoading.value = false
        }
    }

    fun clearChat() {
        _aiMessages.value = listOf(
            ChatMessage("mentor", if (_isBangla.value) "হ্যালো! আমি আপনার বিসিএস এআই মেন্টর। কীভাবে আপনাকে সাহায্য করতে পারি?" else "Hello! I am your BCS AI Mentor. How can I help you today?")
        )
    }

    // Admin Panel Actions
    fun addAnnouncement(text: String) {
        val updated = _announcements.value.toMutableList()
        updated.add(0, text)
        _announcements.value = updated
    }

    fun updateSubjectProgress(subject: SubjectProgress) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSubjectProgress(subject)
        }
    }
}

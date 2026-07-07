package com.example.data.repository

import com.example.data.local.AppDao
import com.example.data.model.Task
import com.example.data.model.SubjectProgress
import com.example.data.model.McqQuestion
import com.example.data.model.MockTestResult
import com.example.data.model.Note
import com.example.data.model.CurrentAffairItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AppRepository(private val appDao: AppDao) {

    val allTasks: Flow<List<Task>> = appDao.getAllTasks()
    val allSubjectProgress: Flow<List<SubjectProgress>> = appDao.getAllSubjectProgress()
    val allMcqs: Flow<List<McqQuestion>> = appDao.getAllMcqs()
    val bookmarkedMcqs: Flow<List<McqQuestion>> = appDao.getBookmarkedMcqs()
    val allMockTestResults: Flow<List<MockTestResult>> = appDao.getAllMockTestResults()
    val allNotes: Flow<List<Note>> = appDao.getAllNotes()
    val allCurrentAffairs: Flow<List<CurrentAffairItem>> = appDao.getAllCurrentAffairs()
    val bookmarkedCurrentAffairs: Flow<List<CurrentAffairItem>> = appDao.getBookmarkedCurrentAffairs()

    suspend fun insertTask(task: Task) = appDao.insertTask(task)
    suspend fun updateTask(task: Task) = appDao.updateTask(task)
    suspend fun deleteTaskById(id: Int) = appDao.deleteTaskById(id)

    suspend fun updateSubjectProgress(progress: SubjectProgress) = appDao.updateSubjectProgress(progress)

    suspend fun insertMockTestResult(result: MockTestResult) = appDao.insertMockTestResult(result)

    suspend fun insertNote(note: Note) = appDao.insertNote(note)
    suspend fun deleteNoteById(id: Int) = appDao.deleteNoteById(id)
    fun searchNotes(query: String): Flow<List<Note>> = appDao.searchNotes(query)

    suspend fun updateMcq(mcq: McqQuestion) = appDao.updateMcq(mcq)
    suspend fun updateCurrentAffair(item: CurrentAffairItem) = appDao.updateCurrentAffair(item)

    fun getMcqsBySubject(subjectId: String): Flow<List<McqQuestion>> = appDao.getMcqsBySubject(subjectId)

    suspend fun populateInitialDataIfEmpty() {
        // Check if subject progress table is empty
        val currentProgressList = allSubjectProgress.first()
        if (currentProgressList.isEmpty()) {
            val subjects = listOf(
                SubjectProgress("bangla", "বাংলা ভাষা ও সাহিত্য", "Bangla Language & Literature", 0.0f, 15, 0),
                SubjectProgress("english", "ইংরেজি ভাষা ও সাহিত্য", "English Language & Literature", 0.0f, 12, 0),
                SubjectProgress("math", "গাণিতিক যুক্তি", "Mathematical Reasoning", 0.0f, 10, 0),
                SubjectProgress("bangladesh", "বাংলাদেশ বিষয়াবলি", "Bangladesh Affairs", 0.0f, 20, 0),
                SubjectProgress("international", "আন্তর্জাতিক বিষয়াবলি", "International Affairs", 0.0f, 15, 0),
                SubjectProgress("science", "সাধারণ বিজ্ঞান", "General Science", 0.0f, 10, 0),
                SubjectProgress("ict", "কম্পিউটার ও তথ্যপ্রযুক্তি", "Computer & ICT", 0.0f, 10, 0),
                SubjectProgress("mental_ability", "মানসিক দক্ষতা", "Mental Ability", 0.0f, 8, 0),
                SubjectProgress("current_affairs", "সাম্প্রতিক বিষয়াবলি", "Current Affairs", 0.0f, 5, 0)
            )
            appDao.insertSubjectProgress(subjects)
        }

        // Check if MCQ table is empty
        val currentMcqs = allMcqs.first()
        if (currentMcqs.isEmpty()) {
            val initialMcqs = listOf(
                McqQuestion(
                    id = 1,
                    subjectId = "bangla",
                    topic = "চর্যাপদ",
                    question = "চর্যাপদ কোন ছন্দে লেখা?",
                    questionBangla = "চর্যাপদ কোন ছন্দে লেখা?",
                    optionsString = "মাত্রাবৃত্ত|অক্ষরবৃত্ত|স্বরবৃত্ত|মুক্তক",
                    optionsStringBangla = "মাত্রাবৃত্ত|অক্ষরবৃত্ত|স্বরবৃত্ত|মুক্তক",
                    correctOptionIndex = 0,
                    explanation = "চর্যাপদের অধিকাংশ পদই মাত্রাবৃত্ত ছন্দে রচিত। এটি বাংলা সাহিত্যের প্রাচীনতম কাব্যগ্রন্থ ও আদি নিদর্শন।",
                    difficulty = "Medium"
                ),
                McqQuestion(
                    id = 2,
                    subjectId = "bangla",
                    topic = "বাংলা ব্যাকরণ",
                    question = "বাংলা ভাষায় যৌগিক স্বরধ্বনি কয়টি?",
                    questionBangla = "বাংলা ভাষায় যৌগিক স্বরধ্বনি কয়টি?",
                    optionsString = "২টি|২৫টি|১১টি|৭টি",
                    optionsStringBangla = "২টি|২৫টি|১১টি|৭টি",
                    correctOptionIndex = 1,
                    explanation = "বাংলা ভাষায় মোট যৌগিক স্বরধ্বনি ২৫টি। তবে মৌলিক যৌগিক স্বরবর্ণ দুটি: ঐ এবং ঔ।",
                    difficulty = "Easy"
                ),
                McqQuestion(
                    id = 3,
                    subjectId = "english",
                    topic = "Literature",
                    question = "Who is the author of 'Hamlet'?",
                    questionBangla = "'Hamlet' নাটকের রচয়িতা কে?",
                    optionsString = "William Shakespeare|John Milton|Christopher Marlowe|Ben Jonson",
                    optionsStringBangla = "উইলিয়াম শেক্সপিয়র|জন মিল্টন|ক্রিস্টোফার মার্লো|বেন জনসন",
                    correctOptionIndex = 0,
                    explanation = "Hamlet is a tragedy written by William Shakespeare around 1599-1601.",
                    difficulty = "Easy"
                ),
                McqQuestion(
                    id = 4,
                    subjectId = "english",
                    topic = "Idioms & Phrases",
                    question = "What is the meaning of 'A man of straw'?",
                    questionBangla = "'A man of straw' প্রবাদের অর্থ কী?",
                    optionsString = "A man of no substance|A very active man|A wealthy person|A strong man",
                    optionsStringBangla = "মূল্যহীন মানুষ|খুব কর্মঠ ব্যক্তি|ধনী ব্যক্তি|শক্তিশালী মানুষ",
                    correctOptionIndex = 0,
                    explanation = "'A man of straw' means a person of no substance, character, or influence.",
                    difficulty = "Medium"
                ),
                McqQuestion(
                    id = 5,
                    subjectId = "math",
                    topic = "শতকরা",
                    question = "একটি দ্রব্যের দাম ২৫% বৃদ্ধি পাওয়ায় তার ব্যবহার শতকরা কত কমালে খরচের কোনো পরিবর্তন হবে না?",
                    questionBangla = "একটি দ্রব্যের দাম ২৫% বৃদ্ধি পাওয়ায় তার ব্যবহার শতকরা কত কমালে খরচের কোনো পরিবর্তন হবে না?",
                    optionsString = "২০%|২৫%|১৫%|৩০%",
                    optionsStringBangla = "২০%|২৫%|১৫%|৩০%",
                    correctOptionIndex = 0,
                    explanation = "সূত্র: (বৃদ্ধি / (১০০ + বৃদ্ধি)) * ১০০ = (২৫ / ১২৫) * ১০০ = ২০%",
                    difficulty = "Medium"
                ),
                McqQuestion(
                    id = 6,
                    subjectId = "bangladesh",
                    topic = "ইতিহাস ও ঐতিহ্য",
                    question = "ঐতিহাসিক ৬-দফা দাবি কোথায় পেশ করা হয়?",
                    questionBangla = "ঐতিহাসিক ৬-দফা দাবি কোথায় পেশ করা হয়?",
                    optionsString = "লাহোরে|ঢাকায়|করাচিতে|দিল্লিতে",
                    optionsStringBangla = "লাহোরে|ঢাকায়|করাচিতে|দিল্লিতে",
                    correctOptionIndex = 0,
                    explanation = "১৯৬৬ সালের ৫-৬ ফেব্রুয়ারি লাহোরে অনুষ্ঠিত বিরোধী দলসমূহের এক সম্মেলনে বঙ্গবন্ধু শেখ মুজিবুর রহমান ঐতিহাসিক ৬-দফা দাবি উত্থাপন করেন।",
                    difficulty = "Easy"
                ),
                McqQuestion(
                    id = 7,
                    subjectId = "international",
                    topic = "জাতিসংঘ",
                    question = "জাতিসংঘের বর্তমান মহাসচিব অ্যান্টোনিও গুতেরেস কোন দেশের নাগরিক?",
                    questionBangla = "জাতিসংঘের বর্তমান মহাসচিব অ্যান্টোনিও গুতেরেস কোন দেশের নাগরিক?",
                    optionsString = "পর্তুগাল|ব্রাজিল|স্পেন|ফ্রান্স",
                    optionsStringBangla = "পর্তুগাল|ব্রাজিল|স্পেন|ফ্রান্স",
                    correctOptionIndex = 0,
                    explanation = "অ্যান্টোনিও গুতেরেস পর্তুগালের প্রাক্তন প্রধানমন্ত্রী এবং জাতিসংঘের বর্তমান ৯ম মহাসচিব।",
                    difficulty = "Easy"
                ),
                McqQuestion(
                    id = 8,
                    subjectId = "science",
                    topic = "মানবদেহ",
                    question = "রক্তে অক্সিজেনের বাহক কে?",
                    questionBangla = "রক্তে অক্সিজেনের বাহক কে?",
                    optionsString = "হিমোগ্লোবিন|শ্বেতকণিকা|অনুচক্রিকা|প্লাজমা",
                    optionsStringBangla = "হিমোগ্লোবিন|শ্বেতকণিকা|অনুচক্রিকা|প্লাজমা",
                    correctOptionIndex = 0,
                    explanation = "লোহিত রক্তকণিকায় অবস্থিত হিমোগ্লোবিন ফুসফুস থেকে সারা দেহে অক্সিজেন পরিবহন করে।",
                    difficulty = "Easy"
                ),
                McqQuestion(
                    id = 9,
                    subjectId = "ict",
                    topic = "কম্পিউটার নেটওয়ার্ক",
                    question = "নিচের কোনটি একটি ক্লাউড স্টোরেজ প্ল্যাটফর্ম?",
                    questionBangla = "নিচের কোনটি একটি ক্লাউড স্টোরেজ প্ল্যাটফর্ম?",
                    optionsString = "Google Drive|HTML|C++|Windows 10",
                    optionsStringBangla = "গুগল ড্রাইভ|এইচটিএমএল|সি++|উইন্ডোজ ১০",
                    correctOptionIndex = 0,
                    explanation = "Google Drive একটি জনপ্রিয় ক্লাউড ফাইল হোস্টিং ও স্টোরেজ সার্ভিস।",
                    difficulty = "Easy"
                )
            )
            appDao.insertMcqs(initialMcqs)
        }

        // Check if Current Affairs table is empty
        val currentAffairs = allCurrentAffairs.first()
        if (currentAffairs.isEmpty()) {
            val initialAffairs = listOf(
                CurrentAffairItem(
                    title = "Signing of Comprehensive Economic Partnership Agreement (CEPA)",
                    titleBangla = "বাংলাদেশ-ভারত ব্যাপক অর্থনৈতিক অংশীদারিত্ব চুক্তি (CEPA)",
                    content = "Bangladesh and India have launched formal negotiations to sign a Comprehensive Economic Partnership Agreement (CEPA) to boost trade and bilateral investments post-LDC graduation.",
                    contentBangla = "স্বল্পোন্নত দেশ (LDC) থেকে উত্তরণের পর দ্বিপাক্ষিক বাণিজ্য ও বিনিয়োগ বাড়াতে বাংলাদেশ ও ভারতের মধ্যে ব্যাপক অর্থনৈতিক অংশীদারিত্ব চুক্তি (CEPA) স্বাক্ষরের চূড়ান্ত আলোচনা শুরু হয়েছে।",
                    category = "Bangladesh",
                    date = "July 2026"
                ),
                CurrentAffairItem(
                    title = "UN Climate Change Conference (COP 31) Preparation",
                    titleBangla = "৩১তম জাতিসংঘ জলবায়ু পরিবর্তন সম্মেলন (COP 31) প্রস্তুতি",
                    content = "Host nations are setting up milestones focusing on renewable energy targets and carbon neutrality pledges for the upcoming COP31 conference.",
                    contentBangla = "আসন্ন COP31 জলবায়ু সম্মেলনকে সামনে রেখে আয়োজক দেশগুলো নবায়নযোগ্য জ্বালানির ব্যবহার এবং কার্বন নির্গমন কমানোর প্রতিশ্রুতি নিয়ে নতুন মাইলফলক নির্ধারণ করছে।",
                    category = "International",
                    date = "June 2026"
                )
            )
            appDao.insertCurrentAffairs(initialAffairs)
        }

        // Check if Tasks/Planner has initial values to guide the user
        val currentTasks = allTasks.first()
        if (currentTasks.isEmpty()) {
            appDao.insertTask(Task(title = "পড়ুন: চর্যাপদের ইতিহাস ও গুরুত্বপূর্ণ কবিদের পরিচয়", category = "Daily", dueDate = "আজ"))
            appDao.insertTask(Task(title = "ইংলিশ গ্রামার: Subject-Verb Agreement ও এর প্র্যাকটিস", category = "Daily", dueDate = "আজ"))
            appDao.insertTask(Task(title = "গণিত: শতকরা অধ্যায়ের ৫টি শর্টকাট সূত্র রিভিশন দিন", category = "Weekly", dueDate = "এই সপ্তাহ"))
        }
    }
}

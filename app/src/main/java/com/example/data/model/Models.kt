package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "Daily", "Weekly", "Monthly", "Yearly"
    val dueDate: String,
    val isCompleted: Boolean = false,
    val completionDate: String? = null
)

@Entity(tableName = "subject_progress")
data class SubjectProgress(
    @PrimaryKey val subjectId: String,
    val titleBangla: String,
    val titleEnglish: String,
    val progress: Float = 0f, // 0.0 to 1.0
    val topicCount: Int = 10,
    val completedTopicCount: Int = 0
)

@Entity(tableName = "mcqs")
data class McqQuestion(
    @PrimaryKey val id: Int,
    val subjectId: String,
    val topic: String,
    val question: String,
    val questionBangla: String,
    val optionsString: String, // Pipe-separated options (A|B|C|D)
    val optionsStringBangla: String,
    val correctOptionIndex: Int,
    val explanation: String,
    val difficulty: String, // "Easy", "Medium", "Hard"
    val isBookmarked: Boolean = false,
    val lastAnsweredCorrectly: Boolean? = null
) {
    fun getOptions(): List<String> = optionsString.split("|")
    fun getOptionsBangla(): List<String> = optionsStringBangla.split("|")
}

@Entity(tableName = "mock_test_results")
data class MockTestResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val testTitle: String,
    val dateMillis: Long = System.currentTimeMillis(),
    val totalQuestions: Int,
    val score: Float,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val durationSeconds: Int,
    val weakSubject: String? = null
)

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val subjectId: String,
    val dateCreated: Long = System.currentTimeMillis(),
    val imagePath: String? = null
)

@Entity(tableName = "current_affairs")
data class CurrentAffairItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val titleBangla: String,
    val content: String,
    val contentBangla: String,
    val category: String, // "Bangladesh", "International"
    val date: String,
    val isBookmarked: Boolean = false
)

data class ResourceItem(
    val title: String,
    val titleBangla: String,
    val type: String, // "PDF", "Video", "Book", "Syllabus", "Circular"
    val description: String,
    val descriptionBangla: String,
    val downloadUrlOrLink: String
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val score: Int,
    val isCurrentUser: Boolean = false
)
